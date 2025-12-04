ALTER TABLE series DROP FOREIGN KEY series_ibfk_1;
ALTER TABLE series ADD CONSTRAINT fk_series_content_cascade
    FOREIGN KEY (content_id) REFERENCES content(content_id) ON DELETE CASCADE;