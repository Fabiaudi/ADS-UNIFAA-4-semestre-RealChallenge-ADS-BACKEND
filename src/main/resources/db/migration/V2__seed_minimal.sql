-- ==============================================
-- V2__seed_dados_iniciais.sql
-- Admin único, 8 Polos, Matérias do curso e Alunos por Polo
-- ==============================================

-- ============ ADMIN (usuário único) ============
-- senha: hash bcrypt placeholder (trocaremos ao implementar Auth)
INSERT INTO users (id, name, email, password_hash, type)
VALUES
  ('A00001', 'Administrador do Sistema', 'admin@unifaa.edu.br',
   '$2a$10$QeQ3PMe1tq9qUOaeP2zXJ.mO9wKpJ4Qm0mX2w6mA1r3S6H2vYqTCO',
   'ADMIN')
ON CONFLICT (id) DO NOTHING;

-- ============ POLOS ============
INSERT INTO users (id, name, email, password_hash, type, location, availability) VALUES
  ('P00001', 'Polo Barra do Piraí',  'polo.barradopirai@unifaa.edu.br',  '$2a$10$QeQ3PMe1tq9qUOaeP2zXJ.mO9wKpJ4Qm0mX2w6mA1r3S6H2vYqTCO', 'POLO', 'Barra do Piraí', 3),
  ('P00002', 'Polo Barra Mansa',     'polo.barramansa@unifaa.edu.br',    '$2a$10$QeQ3PMe1tq9qUOaeP2zXJ.mO9wKpJ4Qm0mX2w6mA1r3S6H2vYqTCO', 'POLO', 'Barra Mansa',    3),
  ('P00003', 'Polo Paty do Alferes', 'polo.patyalferes@unifaa.edu.br',   '$2a$10$QeQ3PMe1tq9qUOaeP2zXJ.mO9wKpJ4Qm0mX2w6mA1r3S6H2vYqTCO', 'POLO', 'Paty do Alferes',3),
  ('P00004', 'Polo Petrópolis',      'polo.petropolis@unifaa.edu.br',    '$2a$10$QeQ3PMe1tq9qUOaeP2zXJ.mO9wKpJ4Qm0mX2w6mA1r3S6H2vYqTCO', 'POLO', 'Petrópolis',     3),
  ('P00005', 'Polo Resende',         'polo.resende@unifaa.edu.br',       '$2a$10$QeQ3PMe1tq9qUOaeP2zXJ.mO9wKpJ4Qm0mX2w6mA1r3S6H2vYqTCO', 'POLO', 'Resende',        3),
  ('P00006', 'Polo Três Rios',       'polo.tresrios@unifaa.edu.br',      '$2a$10$QeQ3PMe1tq9qUOaeP2zXJ.mO9wKpJ4Qm0mX2w6mA1r3S6H2vYqTCO', 'POLO', 'Três Rios',      3),
  ('P00007', 'Polo Valença',         'polo.valenca@unifaa.edu.br',       '$2a$10$QeQ3PMe1tq9qUOaeP2zXJ.mO9wKpJ4Qm0mX2w6mA1r3S6H2vYqTCO', 'POLO', 'Valença',        3),
  ('P00008', 'Polo Volta Redonda',   'polo.voltaredonda@unifaa.edu.br',  '$2a$10$QeQ3PMe1tq9qUOaeP2zXJ.mO9wKpJ4Qm0mX2w6mA1r3S6H2vYqTCO', 'POLO', 'Volta Redonda',  3)
ON CONFLICT (id) DO NOTHING;

-- ============ MATÉRIAS (subjects) ============
INSERT INTO subjects (id, name) VALUES
  ('10000000-0000-0000-0000-000000000001', 'GAME DEVELOPER - DESENVOLVENDO SEU 1º GAME'),
  ('10000000-0000-0000-0000-000000000003', 'ANDROID DEVELOPER - CONSTRUINDO SEU 1º APP'),
  ('10000000-0000-0000-0000-000000000004', 'BACK-END DEVELOPER - CONHECENDO BANCO DE DADOS E INTEGRANDO APLICAÇÕES'),
  ('10000000-0000-0000-0000-000000000005', 'FRONT-END DEVELOPER - CRIANDO APLICAÇÕES PARA AMBIENTE WEB'),
  ('10000000-0000-0000-0000-000000000006', 'UX/UI MOBILE DEVELOPER - CONSTRUINDO APLICAÇÕES MOBILE COM FOCO NA EXPERIÊNCIA DO USUÁRIO'),
  ('10000000-0000-0000-0000-000000000007', 'SOFTWARE QUALITY ASSURANCE (SQA) - GARANTINDO A QUALIDADE DOS SOFTWARES'),
  ('10000000-0000-0000-0000-000000000008', 'CIBER SECURITY ESSENCIALS - CONHECENDO ESTRATÉGIAS DE DEFESA CIBERNÉTICA'),
  ('10000000-0000-0000-0000-000000000009', 'BUSINESS CHALLENGE - ENGENHARIA DE SOFTWARE I'),
  ('10000000-0000-0000-0000-000000000010', 'WINDOWS SERVER MANAGEMENT (ADMINISTRANDO SERVIÇOS DE SEGURANÇA WINDOWS)'),
  ('10000000-0000-0000-0000-000000000011', 'LINUX SECURITY - IMPLEMENTANDO SERVIÇOS DE SEGURANÇA LINUX'),
  ('10000000-0000-0000-0000-000000000012', 'IOT DEVELOPER - CRIANDO EQUIPAMENTOS CONECTADOS'),
  ('10000000-0000-0000-0000-000000000013', 'CLOUD SECURITY - IMPLEMENTANDO SERVIÇOS EM NUVEM'),
  ('10000000-0000-0000-0000-000000000014', 'IT LAW SPECIALIST - COMPREENDENDO OS ASPECTOS ÉTICOS E LEGAIS, SOCIOECONÔMICOS DO DESENVOLVIMENTO DE SOFTWARE'),
  ('10000000-0000-0000-0000-000000000015', 'IA - MACHINE LEARNING - CONSTRUINDO APLICAÇÕES COM INTELIGÊNCIA ARTIFICIAL'),
  ('10000000-0000-0000-0000-000000000016', 'BUSINESS CHALLENGE - ENGENHARIA DE SOFTWARE II'),
  ('10000000-0000-0000-0000-000000000017', 'IT MANAGER - GERENCIANDO OPERAÇÕES DE TECNOLOGIA'),
  ('10000000-0000-0000-0000-000000000018', 'DESRUPT IR SPECIALIST - EXPLORANDO OS NOVOS HORIZONTES DAS TECNOLOGIAS'),
  ('10000000-0000-0000-0000-000000000019', 'STARTUP MANAGEMENT: CRIANDO STARTUP DATA DRIVEN')
ON CONFLICT (id) DO NOTHING;

-- ============ ALUNOS (STUDENT) ============
-- Padrão de matrícula: E#####; cada aluno vinculado ao seu polo via student_polo_id
INSERT INTO users (id, name, email, password_hash, type, course, student_polo_id) VALUES
  -- P00001 - Barra do Piraí
  ('E00001','Alice Santos','alice.p001@unifaa.edu.br','$2a$10$QeQ3PMe1tq9qUOaeP2zXJ.mO9wKpJ4Qm0mX2w6mA1r3S6H2vYqTCO','STUDENT','ADS','P00001'),
  ('E00002','Bruno Lima','bruno.p001@unifaa.edu.br','$2a$10$QeQ3PMe1tq9qUOaeP2zXJ.mO9wKpJ4Qm0mX2w6mA1r3S6H2vYqTCO','STUDENT','ADS','P00001'),

  -- P00002 - Barra Mansa
  ('E00003','Carla Souza','carla.p002@unifaa.edu.br','$2a$10$QeQ3PMe1tq9qUOaeP2zXJ.mO9wKpJ4Qm0mX2w6mA1r3S6H2vYqTCO','STUDENT','ADS','P00002'),
  ('E00004','Diego Ribeiro','diego.p002@unifaa.edu.br','$2a$10$QeQ3PMe1tq9qUOaeP2zXJ.mO9wKpJ4Qm0mX2w6mA1r3S6H2vYqTCO','STUDENT','ADS','P00002'),

  -- P00003 - Paty do Alferes
  ('E00005','Elaine Melo','elaine.p003@unifaa.edu.br','$2a$10$QeQ3PMe1tq9qUOaeP2zXJ.mO9wKpJ4Qm0mX2w6mA1r3S6H2vYqTCO','STUDENT','ADS','P00003'),
  ('E00006','Felipe Neri','felipe.p003@unifaa.edu.br','$2a$10$QeQ3PMe1tq9qUOaeP2zXJ.mO9wKpJ4Qm0mX2w6mA1r3S6H2vYqTCO','STUDENT','ADS','P00003'),

  -- P00004 - Petrópolis
  ('E00007','Giovana Alves','giovana.p004@unifaa.edu.br','$2a$10$QeQ3PMe1tq9qUOaeP2zXJ.mO9wKpJ4Qm0mX2w6mA1r3S6H2vYqTCO','STUDENT','ADS','P00004'),
  ('E00008','Henrique Dias','henrique.p004@unifaa.edu.br','$2a$10$QeQ3PMe1tq9qUOaeP2zXJ.mO9wKpJ4Qm0mX2w6mA1r3S6H2vYqTCO','STUDENT','ADS','P00004'),

  -- P00005 - Resende
  ('E00009','Isabela Prado','isabela.p005@unifaa.edu.br','$2a$10$QeQ3PMe1tq9qUOaeP2zXJ.mO9wKpJ4Qm0mX2w6mA1r3S6H2vYqTCO','STUDENT','ADS','P00005'),
  ('E00010','João Pedro','joao.p005@unifaa.edu.br','$2a$10$QeQ3PMe1tq9qUOaeP2zXJ.mO9wKpJ4Qm0mX2w6mA1r3S6H2vYqTCO','STUDENT','ADS','P00005'),

  -- P00006 - Três Rios
  ('E00011','Karina Monteiro','karina.p006@unifaa.edu.br','$2a$10$QeQ3PMe1tq9qUOaeP2zXJ.mO9wKpJ4Qm0mX2w6mA1r3S6H2vYqTCO','STUDENT','ADS','P00006'),
  ('E00012','Lucas Rocha','lucas.p006@unifaa.edu.br','$2a$10$QeQ3PMe1tq9qUOaeP2zXJ.mO9wKpJ4Qm0mX2w6mA1r3S6H2vYqTCO','STUDENT','ADS','P00006'),

  -- P00007 - Valença
  ('E00013','Mariana Nunes','mariana.p007@unifaa.edu.br','$2a$10$QeQ3PMe1tq9qUOaeP2zXJ.mO9wKpJ4Qm0mX2w6mA1r3S6H2vYqTCO','STUDENT','ADS','P00007'),
  ('E00014','Nathan Costa','nathan.p007@unifaa.edu.br','$2a$10$QeQ3PMe1tq9qUOaeP2zXJ.mO9wKpJ4Qm0mX2w6mA1r3S6H2vYqTCO','STUDENT','ADS','P00007'),

  -- P00008 - Volta Redonda
  ('E00015','Olívia Teixeira','olivia.p008@unifaa.edu.br','$2a$10$QeQ3PMe1tq9qUOaeP2zXJ.mO9wKpJ4Qm0mX2w6mA1r3S6H2vYqTCO','STUDENT','ADS','P00008'),
  ('E00016','Paulo Henrique','paulohenrique.p008@unifaa.edu.br','$2a$10$QeQ3PMe1tq9qUOaeP2zXJ.mO9wKpJ4Qm0mX2w6mA1r3S6H2vYqTCO','STUDENT','ADS','P00008')
ON CONFLICT (id) DO NOTHING;
