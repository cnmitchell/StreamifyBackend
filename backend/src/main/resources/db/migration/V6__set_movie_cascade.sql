ALTER TABLE movie DROP FOREIGN KEY movie_ibfk_1;
ALTER TABLE movie ADD CONSTRAINT fk_movie_content_cascade
    FOREIGN KEY (content_id) REFERENCES content(content_id) ON DELETE CASCADE;