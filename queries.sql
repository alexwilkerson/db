-- 1. List a company’s workers by names.
select per_name
from person natural join works natural join job
where comp_id = '8' and end_date is null;


