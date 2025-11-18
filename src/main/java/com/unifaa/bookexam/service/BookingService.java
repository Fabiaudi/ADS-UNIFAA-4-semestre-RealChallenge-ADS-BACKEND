package com.unifaa.bookexam.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unifaa.bookexam.model.dto.BookingRequestDTO;
import com.unifaa.bookexam.model.entity.Booking;
import com.unifaa.bookexam.model.entity.Polo;
import com.unifaa.bookexam.model.entity.Student;
import com.unifaa.bookexam.model.entity.Subject;
import com.unifaa.bookexam.model.entity.User;
import com.unifaa.bookexam.model.enums.BookingStatus;
import com.unifaa.bookexam.repository.BookingRepository;
import com.unifaa.bookexam.repository.StudentRepository;
import com.unifaa.bookexam.repository.SubjectQueryRepository;
import com.unifaa.bookexam.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ScheduleService scheduleService;
    private final PoloService poloService;
    private final TimeSlotService timeSlotService;
    private final SubjectQueryRepository subjectRepository;
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;

    @Transactional
    public List<Booking> getMyBookings(String studentId) {
    return bookingRepository.findByStudentId(studentId);
    }

    @Transactional
    public Booking createBooking(String studentId, BookingRequestDTO dto) {

        // 1. Converte os IDs
        LocalDate date = dto.getDate();
        String poloId = dto.getPoloId();
        UUID subjectUuid = UUID.fromString(dto.getSubjectId());

        // 2. Buscar a entidade Subject completa
        Subject subject = subjectRepository.findById(subjectUuid)
        .orElseThrow(() -> new IllegalArgumentException("Disciplina não encontrada."));

        // 3. Buscar Polo completo
        Polo polo = poloService.findById(poloId)
            .orElseThrow(() -> new IllegalArgumentException("Polo não encontrado."));

        // 4. Buscar schedule
        var schedule = scheduleService.findScheduleForSubjectAndPoloOnDate(poloId, subjectUuid, dto.getDate())
            .orElseThrow(() -> new IllegalArgumentException("Nenhum schedule encontrado para essa disciplina/polo na data informada."));
        UUID scheduleId = schedule.getId();


        // 5. Verifica se o time slot existe para aquele schedule+dia/hora
        com.unifaa.bookexam.model.enums.DayOfWeek dow = com.unifaa.bookexam.model.enums.DayOfWeek.fromJava(date.getDayOfWeek());
        LocalTime requestedTime = dto.getTime();

        boolean timeSlotExists = timeSlotService.existsByScheduleAndDayAndStartTime(
            scheduleId,
            dow,
            requestedTime
            );

        if (!timeSlotExists) {
            throw new IllegalArgumentException("Dia/hora não disponível para esse schedule.");
        }

        // 6. Verifica a reserva única por aluno
        Optional<Booking> existing = bookingRepository.findByStudentIdAndSubjectAndPoloAndDateAndTime(studentId, subject, polo, dto.getDate(), dto.getTime());
       

        if (existing.isPresent()) {
            throw new IllegalStateException("Aluno já possui booking para essa combinação.");
        }

        // 7. Verifica capacidade
        int capacity = poloService.getCapacity(poloId);

        long booked = bookingRepository.countByPoloAndSubjectAndDateAndTime(
                polo,
                subject,
                dto.getDate(),
                dto.getTime()
        );

        if (booked >= capacity) {
            throw new IllegalStateException("Horário já lotado.");
        }

        // 8. Cria a reserva

        Student student = studentRepository.findById(studentId)
        .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado."));

        Booking booking = new Booking();
        booking.setStudent(student);
        booking.setSubject(subject);
        booking.setPolo(polo);
        booking.setDate(dto.getDate());
        booking.setTime(dto.getTime());
        booking.setCreatedAt(LocalDate.now());
        booking.setStatus(BookingStatus.CONFIRMED);

        try {
            return bookingRepository.save(booking);
        } catch (DataIntegrityViolationException ex) {
            throw ex;
        }
    }

@Transactional
public void deleteBooking(String requesterEmail, boolean isAdmin, boolean isPolo, String bookingId) {
    UUID bookingUuid = UUID.fromString(bookingId);
    
    Booking booking = bookingRepository.findById(bookingUuid)
        .orElseThrow(() -> new IllegalArgumentException("Booking não encontrado."));
    
        // Regra 1: Polo não pode cancelar
        if (isPolo) {
            throw new SecurityException("Polo não tem permissão para cancelar reservas.");
        }
        
        // Regra 2: Estudante só cancela a própria reserva
        if (!isAdmin) {
            String studentId = booking.getStudent().getId();
        
        // Buscar o ID do usuário pelo email
        User requester = userRepository.findByEmail(requesterEmail)
            .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));
        String requesterId = requester.getId();
        
        if (!studentId.equals(requesterId)) {
            throw new SecurityException("Usuário não autorizado a cancelar essa reserva.");
        }
    }
    
    // Regra 3: Admin pode cancelar qualquer reserva
    booking.setStatus(BookingStatus.CANCELLED);
    bookingRepository.save(booking);
}
}