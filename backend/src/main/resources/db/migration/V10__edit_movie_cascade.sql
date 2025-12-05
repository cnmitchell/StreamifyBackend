ALTER TABLE movie DROP FOREIGN KEY movie_ibfk_2;
ALTER TABLE movie ADD CONSTRAINT fk_movie_content_cascade02
FOREIGN KEY (sequel_to) REFERENCES content(content_id) ON DELETE CASCADE;