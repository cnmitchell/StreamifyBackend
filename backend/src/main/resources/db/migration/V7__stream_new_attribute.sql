ALTER TABLE stream
ADD COLUMN episode_id CHAR(20) NULL;

ALTER TABLE stream
ADD CONSTRAINT fk_stream_episode
FOREIGN KEY (content_id, episode_id)
REFERENCES episode(content_id, episode_id);