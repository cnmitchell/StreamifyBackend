ALTER TABLE admin DROP FOREIGN KEY admin_ibfk_1;
ALTER TABLE admin ADD CONSTRAINT fk_admin_user_cascade
    FOREIGN KEY (email) REFERENCES users(email) ON DELETE CASCADE;

ALTER TABLE member DROP FOREIGN KEY member_ibfk_2;
ALTER TABLE member ADD CONSTRAINT fk_member_user_cascade
    FOREIGN KEY (email) REFERENCES users(email) ON DELETE CASCADE;

ALTER TABLE stream DROP FOREIGN KEY stream_ibfk_1;
ALTER TABLE stream ADD CONSTRAINT fk_stream_user_cascade
    FOREIGN KEY (email) REFERENCES users(email) ON DELETE CASCADE;

ALTER TABLE streamingHistory DROP FOREIGN KEY streamingHistory_ibfk_1;
ALTER TABLE streamingHistory ADD CONSTRAINT fk_history_user_cascade
    FOREIGN KEY (email) REFERENCES users(email) ON DELETE CASCADE;

ALTER TABLE has DROP FOREIGN KEY has_ibfk_2;
ALTER TABLE has ADD CONSTRAINT fk_has_user_cascade
    FOREIGN KEY (email) REFERENCES users(email) ON DELETE CASCADE;

ALTER TABLE awardedTo DROP FOREIGN KEY awardedTo_ibfk_2;
ALTER TABLE awardedTo ADD CONSTRAINT fk_awardedTo_content_cascade
    FOREIGN KEY (content_id) REFERENCES content(content_id) ON DELETE CASCADE;

ALTER TABLE castIn DROP FOREIGN KEY castIn_ibfk_1;
ALTER TABLE castIn ADD CONSTRAINT fk_castIn_content_cascade
    FOREIGN KEY (content_id) REFERENCES content(content_id) ON DELETE CASCADE;

ALTER TABLE directedBy DROP FOREIGN KEY directedBy_ibfk_1;
ALTER TABLE directedBy ADD CONSTRAINT fk_directedBy_content_cascade
    FOREIGN KEY (content_id) REFERENCES content(content_id) ON DELETE CASCADE;

ALTER TABLE stream DROP FOREIGN KEY stream_ibfk_2;
ALTER TABLE stream ADD CONSTRAINT fk_stream_content_cascade
    FOREIGN KEY (content_id) REFERENCES content(content_id) ON DELETE CASCADE;

ALTER TABLE has DROP FOREIGN KEY has_ibfk_1;
ALTER TABLE has ADD CONSTRAINT fk_has_content_cascade
    FOREIGN KEY (content_id) REFERENCES content(content_id) ON DELETE CASCADE;