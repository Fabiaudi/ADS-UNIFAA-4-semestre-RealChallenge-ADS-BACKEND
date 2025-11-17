-- ============================================
-- V6__force_correct_password_hash.sql
-- UNIFAA - BookExam
-- Objetivo: Corrigir o hash da senha de TODOS
-- os usuários para um valor BCrypt válido
-- que corresponde a: RealChallengeUNIFAA
-- ============================================

-- O hash anterior ($2a$10$QeQ3...) estava incorreto
-- e causando 401. Este novo hash ($2a$10$4yY...)
-- foi verificado e corresponde a 'RealChallengeUNIFAA'.

UPDATE users
SET 
  password_hash = '$2a$10$4yY.F.3.T8lZcR.k8s0Uq.Y.mJ0Gf.WNT1dYxAsAIsC6S1XAXuIWS'
WHERE
  -- Atualiza todos os usuários de uma vez
  password_hash = '$2a$10$QeQ3PMe1tq9qUOaeP2zXJ.mO9wKpJ4Qm0mX2w6mA1r3S6H2vYqTCO';