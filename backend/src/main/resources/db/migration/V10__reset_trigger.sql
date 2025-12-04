DROP TRIGGER quit_streaming;

CREATE TRIGGER drop_stream
    AFTER DELETE ON stream
    FOR EACH ROW
BEGIN
    INSERT INTO streamingHistory (email, content_id, episode_id, timestamp)
    VALUES(OLD.email, OLD.content_id, OLD.episode_id, NOW());
END;
