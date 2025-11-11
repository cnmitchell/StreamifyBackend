DELIMITER $$
CREATE TRIGGER quit_streaming
AFTER DELETE ON has
FOR EACH ROW
BEGIN
    INSERT INTO streamingHistory (stream_id, email, content_id, timestamp)
    VALUES(OLD.stream_id, OLD.email, OLD.content_id, NOW());
END$$
DELIMITER ;