-- Insert sample users
-- Nota: Parola pentru toți utilizatorii este "password123" (hash-ul de mai jos)

-- Inserează utilizatori în tabela users (doar coloanele care există)
INSERT INTO users (username, email, password, enabled, role) VALUES
('admin', 'admin@library.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye0IPtgfLB8A/aRVgZnQDZM7MUPtBQQQO', true, 'ADMIN'),
('librarian1', 'librarian1@library.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye0IPtgfLB8A/aRVgZnQDZM7MUPtBQQQO', true, 'LIBRARIAN'),
('librarian2', 'librarian2@library.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye0IPtgfLB8A/aRVgZnQDZM7MUPtBQQQO', true, 'LIBRARIAN'),
('librarian3', 'librarian3@library.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye0IPtgfLB8A/aRVgZnQDZM7MUPtBQQQO', true, 'LIBRARIAN'),
('user1', 'ana.georgescu@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye0IPtgfLB8A/aRVgZnQDZM7MUPtBQQQO', true, 'USER'),
('user2', 'mihai.dumitrescu@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye0IPtgfLB8A/aRVgZnQDZM7MUPtBQQQO', true, 'USER'),
('user3', 'elena.radu@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye0IPtgfLB8A/aRVgZnQDZM7MUPtBQQQO', true, 'USER'),
('user4', 'andrei.stanciu@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye0IPtgfLB8A/aRVgZnQDZM7MUPtBQQQO', true, 'USER'),
('user5', 'cristina.marin@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye0IPtgfLB8A/aRVgZnQDZM7MUPtBQQQO', true, 'USER'),
('user6', 'alexandru.popescu@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye0IPtgfLB8A/aRVgZnQDZM7MUPtBQQQO', true, 'USER'),
('user7', 'diana.ionescu@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye0IPtgfLB8A/aRVgZnQDZM7MUPtBQQQO', true, 'USER'),
('user8', 'radu.stefan@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye0IPtgfLB8A/aRVgZnQDZM7MUPtBQQQO', true, 'USER'),
('user9', 'monica.vasilescu@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye0IPtgfLB8A/aRVgZnQDZM7MUPtBQQQO', true, 'USER'),
('user10', 'bogdan.marinescu@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye0IPtgfLB8A/aRVgZnQDZM7MUPtBQQQO', true, 'USER');

-- Inserează detaliile utilizatorilor în tabela user_details
INSERT INTO user_details (user_id, first_name, last_name, phone_number, address) VALUES
(1, 'Admin', 'Administrator', '+40123456789', 'Admin Street 1, București'),
(2, 'Maria', 'Popescu', '+40123456790', 'Bibliotecii Street 1, București'),
(3, 'Ion', 'Ionescu', '+40123456791', 'Bibliotecii Street 2, București'),
(4, 'Elena', 'Marinescu', '+40123456792', 'Bibliotecii Street 3, București'),
(5, 'Ana', 'Georgescu', '+40123456793', 'Calea Victoriei 12, București'),
(6, 'Mihai', 'Dumitrescu', '+40123456794', 'Bulevardul Unirii 45, București'),
(7, 'Elena', 'Radu', '+40123456795', 'Strada Dorobanti 78, București'),
(8, 'Andrei', 'Stanciu', '+40123456796', 'Piața Amzei 23, București'),
(9, 'Cristina', 'Marin', '+40123456797', 'Strada Franceza 56, București'),
(10, 'Alexandru', 'Popescu', '+40123456798', 'Bulevardul Magheru 89, București'),
(11, 'Diana', 'Ionescu', '+40123456799', 'Calea Floreasca 34, București'),
(12, 'Radu', 'Ștefan', '+40123456800', 'Strada Aviatorilor 67, București'),
(13, 'Monica', 'Vasilescu', '+40123456801', 'Piața Romana 15, București'),
(14, 'Bogdan', 'Marinescu', '+40123456802', 'Bulevardul Kiseleff 91, București');

-- Notă: Parola pentru toți utilizatorii este "password123"
-- Hash-ul BCrypt: $2a$10$N9qo8uLOickgx2ZMRZoMye0IPtgfLB8A/aRVgZnQDZM7MUPtBQQQO