ALTER TABLE member DROP FOREIGN KEY member_ibfk_1;
ALTER TABLE member ADD CONSTRAINT fk_member_plan_cascade
    FOREIGN KEY(subscription_id) REFERENCES subscriptionPlan(subscription_id) ON DELETE CASCADE;

ALTER TABLE castIn DROP FOREIGN KEY castIn_ibfk_2;
ALTER TABLE castIn ADD CONSTRAINT fk_castIn_person_cascade
    FOREIGN KEY (person_id) REFERENCES person(person_id) ON DELETE CASCADE;

ALTER TABLE directedBy DROP FOREIGN KEY directedBy_ibfk_2;
ALTER TABLE directedBy ADD CONSTRAINT fk_directedBy_person_cascade
    FOREIGN KEY (person_id) REFERENCES person(person_id) ON DELETE CASCADE;

ALTER TABLE awardedTo DROP FOREIGN KEY awardedTo_ibfk_1;
ALTER TABLE awardedTo ADD CONSTRAINT fk_awardedTo_award_cascade
    FOREIGN KEY (award_name) REFERENCES award(award_name) ON DELETE CASCADE;
