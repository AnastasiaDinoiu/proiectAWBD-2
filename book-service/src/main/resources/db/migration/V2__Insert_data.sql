-- Insert sample categories
INSERT INTO categories (name, description) VALUES
('Fiction', 'Fictional books including novels and short stories'),
('Science Fiction', 'Science fiction and fantasy books'),
('Biography', 'Biographical and autobiographical works'),
('History', 'Historical books and documentaries'),
('Technology', 'Books about technology and programming'),
('Romance', 'Romantic novels and love stories'),
('Mystery', 'Mystery and detective novels'),
('Fantasy', 'Fantasy and magical realm stories'),
('Horror', 'Horror and thriller books'),
('Classic Literature', 'Classic literary works');

-- Insert sample publishers
INSERT INTO publishers (name, address, phone_number, email, website) VALUES
('Penguin Random House', '1745 Broadway, New York, NY 10019', '+1-212-782-9000', 'info@penguinrandomhouse.com', 'https://www.penguinrandomhouse.com'),
('HarperCollins', '195 Broadway, New York, NY 10007', '+1-212-207-7000', 'info@harpercollins.com', 'https://www.harpercollins.com'),
('Simon & Schuster', '1230 Avenue of the Americas, New York, NY 10020', '+1-212-698-7000', 'info@simonandschuster.com', 'https://www.simonandschuster.com'),
('Macmillan', '120 Broadway, New York, NY 10271', '+1-646-307-5151', 'info@macmillan.com', 'https://us.macmillan.com'),
('Hachette Book Group', '1290 Avenue of the Americas, New York, NY 10104', '+1-212-364-1100', 'info@hbgusa.com', 'https://www.hachettebookgroup.com'),
('Editura Humanitas', 'Piata Presei Libere 1, Bucuresti', '+40-21-408-8300', 'info@humanitas.ro', 'https://www.humanitas.ro'),
('Editura Polirom', 'Bd. Carol I nr. 4, Iasi', '+40-232-214-100', 'polirom@polirom.ro', 'https://www.polirom.ro'),
('Editura Nemira', 'Str. Grigore Alexandrescu 89-97, Bucuresti', '+40-21-230-4040', 'nemira@nemira.ro', 'https://www.nemira.ro'),
('OReilly Media', '1005 Gravenstein Highway North, Sebastopol, CA', '+1-707-827-7000', 'info@oreilly.com', 'https://www.oreilly.com'),
('Packt Publishing', 'Livery Place, 35 Livery Street, Birmingham', '+44-121-265-6484', 'info@packt.com', 'https://www.packtpub.com');

-- Insert sample authors
INSERT INTO authors (first_name, last_name, biography, birth_date, nationality) VALUES
('George', 'Orwell', 'English novelist and essayist, journalist and critic, best known for Animal Farm and 1984.', '1903-06-25', 'British'),
('J.K.', 'Rowling', 'British author, best known for the Harry Potter series of fantasy novels.', '1965-07-31', 'British'),
('Stephen', 'King', 'American author of horror, supernatural fiction, suspense, crime, science-fiction, and fantasy novels.', '1947-09-21', 'American'),
('Agatha', 'Christie', 'English writer known for her detective novels featuring Hercule Poirot and Miss Marple.', '1890-09-15', 'British'),
('Isaac', 'Asimov', 'American writer and professor of biochemistry, known for his works of science fiction and popular science.', '1920-01-02', 'American'),
('Harper', 'Lee', 'American novelist widely known for To Kill a Mockingbird, published in 1960.', '1926-04-28', 'American'),
('F. Scott', 'Fitzgerald', 'American novelist and short story writer, known for The Great Gatsby.', '1896-09-24', 'American'),
('Jane', 'Austen', 'English novelist known for her wit, social commentary, and insight into 19th-century British society.', '1775-12-16', 'British'),
('Gabriel', 'Garcia Marquez', 'Colombian novelist and short-story writer, Nobel Prize winner for One Hundred Years of Solitude.', '1927-03-06', 'Colombian'),
('Toni', 'Morrison', 'American novelist, essayist, and professor, Nobel Prize winner known for Beloved.', '1931-02-18', 'American'),
('Mircea', 'Eliade', 'Romanian historian of religion, fiction writer, philosopher, and professor at the University of Chicago.', '1907-03-13', 'Romanian'),
('Marin', 'Preda', 'Romanian novelist and short story writer, considered one of the most important Romanian prose writers.', '1922-08-05', 'Romanian'),
('Liviu', 'Rebreanu', 'Romanian novelist, playwright, short story writer, and journalist, author of Ion and Padurea spanzuratilor.', '1885-11-27', 'Romanian'),
('Ion', 'Creanga', 'Romanian writer, raconteur and schoolteacher, best known for his Childhood Memories.', '1837-03-01', 'Romanian'),
('Mihai', 'Eminescu', 'Romanian Romantic poet, novelist, and journalist, considered the most important Romanian poet.', '1850-01-15', 'Romanian'),
('Robert C.', 'Martin', 'American software engineer and instructor, known for his books on software development and clean code.', '1952-12-05', 'American'),
('Joshua', 'Bloch', 'American software engineer and technology author, known for his work on Java programming language.', '1961-08-28', 'American'),
('Eric', 'Evans', 'American software engineer and author, known for Domain-Driven Design concepts.', '1962-01-01', 'American');

-- Insert sample books
INSERT INTO books (isbn, title, description, publication_date, pages, available_copies, price, category_id, publisher_id) VALUES
('978-0-452-28423-4', '1984', 'A dystopian social science fiction novel exploring themes of totalitarianism, surveillance, and individuality.', '1949-06-08', 328, 5, 12.99, 1, 1),
('978-0-439-70818-8', 'Harry Potter and the Philosophers Stone', 'The first novel in the Harry Potter series, introducing the magical world of Hogwarts.', '1997-06-26', 223, 8, 15.99, 8, 2),
('978-0-307-74434-6', 'The Shining', 'A horror novel about a family winter stay at an isolated hotel in the Colorado Rockies.', '1977-01-28', 447, 3, 14.99, 9, 3),
('978-0-062-07348-6', 'Murder on the Orient Express', 'A detective novel featuring Hercule Poirot solving a murder aboard the famous train.', '1934-01-01', 256, 4, 13.99, 7, 4),
('978-0-553-29337-0', 'Foundation', 'The first novel in Asimovs Foundation series, exploring psychohistory and galactic empires.', '1951-05-01', 244, 6, 16.99, 2, 5),
('978-0-06-112008-4', 'To Kill a Mockingbird', 'A coming-of-age story dealing with serious issues of rape and racial inequality in the American South.', '1960-07-11', 376, 7, 13.99, 1, 2),
('978-0-7432-7356-5', 'The Great Gatsby', 'A classic American novel set in the Jazz Age, exploring themes of wealth, love, and the American Dream.', '1925-04-10', 180, 5, 12.99, 10, 3),
('978-0-14-143951-8', 'Pride and Prejudice', 'A romantic novel following Elizabeth Bennets evolving relationship with the proud Mr. Darcy.', '1813-01-28', 432, 6, 11.99, 6, 1),
('978-0-06-088328-7', 'One Hundred Years of Solitude', 'A landmark novel telling the multi-generational story of the Buendia family in Macondo.', '1967-05-30', 417, 4, 14.99, 1, 2),
('978-0-307-26451-1', 'Beloved', 'A powerful novel about the psychological trauma of slavery and its aftermath.', '1987-09-02', 324, 3, 13.99, 1, 3),
('978-973-50-0123-4', 'Maitreyi', 'A semi-autobiographical novel about a love story between a European and an Indian woman.', '1933-01-01', 280, 5, 25.99, 6, 6),
('978-973-46-0234-5', 'Morometii', 'A classic Romanian novel depicting rural life and social changes in early 20th century Romania.', '1955-01-01', 520, 4, 28.99, 1, 7),
('978-973-47-0345-6', 'Ion', 'A masterpiece of Romanian realist literature exploring the relationship between man and land.', '1920-01-01', 480, 6, 26.99, 1, 6),
('978-973-48-0456-7', 'Amintiri din copilarie', 'Charming and nostalgic childhood memories of rural Romanian life.', '1881-01-01', 180, 8, 18.99, 3, 8),
('978-973-49-0567-8', 'Poezii', 'Complete collection of poems by Romanias national poet, showcasing romantic themes and melancholy.', '1883-01-01', 450, 7, 32.99, 10, 7),
('978-0-13-235088-4', 'Clean Code: A Handbook of Agile Software Craftsmanship', 'A guide to writing readable, maintainable, and efficient code for software developers.', '2008-08-01', 464, 10, 45.99, 5, 9),
('978-0-321-35668-0', 'Effective Java', 'Best practices for the Java programming language from one of its architects.', '2008-05-08', 346, 12, 42.99, 5, 9),
('978-0-321-12521-7', 'Domain-Driven Design: Tackling Complexity in the Heart of Software', 'A comprehensive guide to building complex software systems using domain-driven design principles.', '2003-08-20', 560, 8, 55.99, 5, 10);

-- Insert book-author relationships
INSERT INTO book_authors (book_id, author_id) VALUES
(1, 1),   -- 1984 by George Orwell
(2, 2),   -- Harry Potter by J.K. Rowling
(3, 3),   -- The Shining by Stephen King
(4, 4),   -- Murder on the Orient Express by Agatha Christie
(5, 5),   -- Foundation by Isaac Asimov
(6, 6),   -- To Kill a Mockingbird by Harper Lee
(7, 7),   -- The Great Gatsby by F. Scott Fitzgerald
(8, 8),   -- Pride and Prejudice by Jane Austen
(9, 9),   -- One Hundred Years of Solitude by Gabriel Garcia Marquez
(10, 10), -- Beloved by Toni Morrison
(11, 11), -- Maitreyi by Mircea Eliade
(12, 12), -- Morometii by Marin Preda
(13, 13), -- Ion by Liviu Rebreanu
(14, 14), -- Amintiri din copilarie by Ion Creanga
(15, 15), -- Poezii by Mihai Eminescu
(16, 16), -- Clean Code by Robert C. Martin
(17, 17), -- Effective Java by Joshua Bloch
(18, 18); -- Domain-Driven Design by Eric Evans