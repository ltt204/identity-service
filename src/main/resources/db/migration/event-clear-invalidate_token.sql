create event clean_invalidated_token
	on schedule
		every 10 second
	comment 'Clear out every logged out token of user which is expired'
	do
delete from identity_service.invalidated_token where expiration_time <= now();

alter event clean_invalidated_token enable;

SHOW EVENTS FROM identity_service;