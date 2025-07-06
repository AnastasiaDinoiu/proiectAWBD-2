-- Insert sample loans
INSERT INTO loans (user_id, book_id, loan_date, due_date, return_date, status, notes) VALUES
-- Active loans (împrumuturi active)
(5, 1, '2024-06-20', '2024-07-04', NULL, 'ACTIVE', 'Regular loan - first time borrowing 1984'),
(6, 2, '2024-06-25', '2024-07-09', NULL, 'ACTIVE', 'Harry Potter fan - extended due to student status'),
(7, 3, '2024-06-28', '2024-07-12', NULL, 'ACTIVE', 'Stephen King enthusiast'),
(8, 5, '2024-07-01', '2024-07-15', NULL, 'ACTIVE', 'Science fiction lover'),
(9, 8, '2024-07-02', '2024-07-16', NULL, 'ACTIVE', 'Classic literature student'),
(10, 11, '2024-07-03', '2024-07-17', NULL, 'ACTIVE', 'Romanian literature research'),
(11, 12, '2024-07-04', '2024-07-18', NULL, 'ACTIVE', 'High school literature assignment'),
(12, 15, '2024-07-05', '2024-07-19', NULL, 'ACTIVE', 'Poetry enthusiast'),

-- Returned loans (împrumuturi returnate)
(5, 4, '2024-05-15', '2024-05-29', '2024-05-28', 'RETURNED', 'Returned on time - loved Agatha Christie'),
(6, 6, '2024-05-20', '2024-06-03', '2024-06-01', 'RETURNED', 'Early return - powerful story'),
(7, 7, '2024-04-10', '2024-04-24', '2024-04-25', 'RETURNED', 'Returned one day late - worth it'),
(8, 9, '2024-03-15', '2024-03-29', '2024-03-20', 'RETURNED', 'Early return - Garcia Marquez is amazing'),
(9, 10, '2024-04-01', '2024-04-15', '2024-04-14', 'RETURNED', 'Toni Morrison masterpiece'),
(10, 13, '2024-05-01', '2024-05-15', '2024-05-13', 'RETURNED', 'Classic Romanian literature'),
(11, 14, '2024-05-10', '2024-05-24', '2024-05-22', 'RETURNED', 'Childhood memories - beautiful'),
(12, 1, '2024-04-20', '2024-05-04', '2024-05-02', 'RETURNED', 'Dystopian masterpiece'),
(13, 2, '2024-03-25', '2024-04-08', '2024-04-06', 'RETURNED', 'Started the HP journey'),
(14, 7, '2024-05-25', '2024-06-08', '2024-06-07', 'RETURNED', 'American Dream analysis'),

-- Overdue loans (împrumuturi întârziate)
(13, 4, '2024-06-01', '2024-06-15', NULL, 'OVERDUE', 'User contacted, extension requested - family emergency'),
(14, 6, '2024-05-25', '2024-06-08', NULL, 'OVERDUE', 'Multiple reminders sent - traveling abroad'),
(5, 9, '2024-06-10', '2024-06-24', NULL, 'OVERDUE', 'Lost book reported, replacement ordered'),

-- Historical loans (old returned loans)
(6, 1, '2024-02-01', '2024-02-15', '2024-02-14', 'RETURNED', 'First introduction to Orwell'),
(7, 2, '2024-02-10', '2024-02-24', '2024-02-20', 'RETURNED', 'Magic begins here'),
(8, 3, '2024-02-15', '2024-03-01', '2024-02-28', 'RETURNED', 'Horror at its finest'),
(9, 5, '2024-01-20', '2024-02-03', '2024-02-01', 'RETURNED', 'Sci-fi foundation'),
(10, 8, '2024-01-25', '2024-02-08', '2024-02-07', 'RETURNED', 'Austen never disappoints'),
(11, 11, '2024-01-30', '2024-02-13', '2024-02-12', 'RETURNED', 'Romanian cultural heritage'),
(12, 12, '2024-02-05', '2024-02-19', '2024-02-18', 'RETURNED', 'Rural life masterpiece'),
(13, 13, '2024-02-12', '2024-02-26', '2024-02-25', 'RETURNED', 'Ion - character study'),
(14, 14, '2024-02-20', '2024-03-05', '2024-03-03', 'RETURNED', 'Nostalgia in literature'),
(5, 15, '2024-03-01', '2024-03-15', '2024-03-14', 'RETURNED', 'Eminescu poetry collection');