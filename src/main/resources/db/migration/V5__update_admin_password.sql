-- ============================================
-- V5__update_admin_password.sql
-- UNIFAA - BookExam
-- Objetivo: Atualizar o hash da senha do usuário
-- admin principal para um valor conhecido.
--
-- Email: admin@unifaa.edu.br
-- Senha: RealChallengeUNIFAA
-- ============================================

UPDATE users
SET 
  -- Hash BCrypt para a senha: RealChallengeUNIFAA
  password_hash = '$2a$10$QeQ3PMe1tq9qUOaeP2zXJ.mO9wKpJ4Qm0mX2w6mA1r3S6H2vYqTCO'
WHERE 
  email = 'admin@unifaa.edu.br';