DELIMITER $$
	DROP PROCEDURE IF EXISTS crea_resum_facturacio $$
    CREATE PROCEDURE crea_resum_facturacio(IN p_mes INT, p_any INT)
    BEGIN
		DECLARE v_dni_client VARCHAR(8);
		DECLARE v_total FLOAT(6,2);
		DECLARE exit_loop BOOLEAN;

        DECLARE client_cursor CURSOR FOR
			-- SQL Sentence to get a query --
			select dni_client, format(sum(preu_total), 2) as total from comandes where extract(month from data)=p_mes and
            extract(year from data)=p_any group by dni_client;

		DECLARE CONTINUE HANDLER FOR
			NOT FOUND SET exit_loop = TRUE;

		-- open cursor --
		OPEN client_cursor;

        -- open loop --
        client_loop: LOOP

        -- use the cursor to iterate over the set variables that save each value got from SQL Sentence --
        FETCH client_cursor INTO v_dni_client, v_total;

			IF exit_loop THEN
				-- Close cursor --
				CLOSE client_cursor;
                -- Leave loop --
				LEAVE client_loop;
			END IF;

            -- run an action with the got query (register by register) --
            INSERT INTO resum_facturacio (mes,any,dni_client_resum, total_mes) values (p_mes,p_any, v_dni_client, v_total);
        END LOOP client_loop;
	   END $$
DELIMITER $$ ;

call crea_resum_facturacio(12,2020);
