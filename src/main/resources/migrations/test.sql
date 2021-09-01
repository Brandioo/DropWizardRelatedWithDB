--liquibase formatted sql
--changeset brand:1

UPDATE provtrainingslinks SET formid="212097016771050", link="https://form.jotform.com/212097016771050?pid={{pid}}&state=WV" WHERE state="WV";

-- rollback SET FOREIGN_KEY_CHECKS = 0;
-- rollback UPDATE provtrainingslinks SET formid="91826104509153", link="https://form.jotform.com/91826104509153?pid={{pid}}" WHERE state="WV";
-- rollback SET FOREIGN_KEY_CHECKS = 1;
