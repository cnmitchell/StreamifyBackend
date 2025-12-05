ALTER TABLE streamingHistory DROP FOREIGN KEY streamingHistory_ibfk_2;
ALTER TABLE streamingHistory ADD CONSTRAINT fk_streamingHistory_content_cascade
FOREIGN KEY (content_id) REFERENCES content(content_id)
ON DELETE CASCADE;