CREATE TABLE commitment(
    id SERIAL PRIMARY KEY,
    user_id NUMERIC NOT NULL,
    status VARCHAR(16) NOT NULL,
    book_title VARCHAR(64) NOT NULL,
    book_total_pages NUMERIC NOT NULL,
    book_current_page NUMERIC NOT NULL,
    start_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    deadline TIMESTAMP WITHOUT TIME ZONE NOT NULL
);
