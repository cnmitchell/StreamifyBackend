DELIMITER &&

CREATE TRIGGER reject_str
BEFORE INSERT ON stream
FOR EACH ROW
BEGIN
    DECLARE limit INT;
    DECLARE curr INT;

    SELECT
        sp.active_streams
    INTO
        limit
    FROM
        member m
    JOIN
        subscriptionPlan sp ON m.subscription_id = sp.subscription_id
    WHERE
        m.email = NEW.email;

    IF limit IS NULL THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Subscription Error: User must have an active membership to stream.';
    END IF;

    SELECT
        COUNT(*)
    INTO
        curr
    FROM
        stream s
    WHERE
        s.email = NEW.email;

    IF curr >= limit THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Stream limit exceeded: Cannot start a new stream under the current subscription plan.';
    END IF;
END &&

DELIMITER ;



