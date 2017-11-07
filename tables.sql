create table person(
    per_id numeric(8,0),
    per_name varchar(100) not null,
    apt_number varchar(100),
    street_number numeric(20,0),
    street_name varchar(100),
    city varchar(100),
    state varchar(50),
    zip_code numeric(5,0),
    email varchar(100),
    gender varchar(6) check(gender = 'male' or gender = 'female'),
    primary key(per_id)
);

create table phone(
    per_id numeric(8,0),
    phone varchar(20),
    p_type varchar(5) check(p_type = 'cell' or p_type = 'home' or p_type = 'work' or p_type = 'other'),
    primary key(per_id, phone),
    foreign key(per_id) references person on delete set null
);

create table company(
    comp_id varchar(8),
    name varchar(100),
    street_number numeric(10,0),
    street_name varchar(100),
    city varchar(100),
    state varchar(50),
    zip_code numeric(5,0),
    primary_sector varchar(100),
    website varchar(100),
    primary key(comp_id)
);

create table job(
    job_code varchar(8),
    comp_id varchar(8),
    emp_mode varchar(9) check(emp_mode = 'full-time' or emp_mode = 'part-time'),
    pay_rate numeric(8,2) check(pay_rate > 0),
    pay_type varchar(8) check(pay_type = 'wage' or pay_type = 'salary'),
    primary key(job_code),
    foreign key(comp_id) references company on delete cascade
);

create table works(
    job_code varchar(8),
    per_id numeric(8,0),
    start_date date,
    end_date date,
    primary key(job_code, per_id, start_date),
    foreign key(per_id) references person on delete cascade,
    foreign key(job_code) references job on delete cascade
);

create table naics(
    naics_code numeric(6,0),
    title varchar(100),
    primary key(naics_code)
);

create table company_naics(
    comp_id varchar(8),
    naics_code numeric(6,0),
    primary key(comp_id, naics_code),
    foreign key(naics_code) references naics on delete cascade
);

create table job_category(
    cate_code varchar(8),
    title varchar(100),
    description varchar(255),
    pay_range_high numeric(8,2) check (pay_range_high > 0),
    pay_range_low numeric(8,2) check (pay_range_low > 0),
    parent_cate varchar(8),
    primary key(cate_code)
);

create table has_parent(
    cate_code varchar(8),
    parent_cate varchar(8),
    primary key(cate_code, parent_cate),
    foreign key(cate_code) references job_category on delete cascade
);

create table certificate(
    cert_code varchar(20),
    title varchar(100) not null,
    description varchar(500),
    expire_date date,
    issued_by varchar(20),
    t_code varchar(20),
    primary key(cert_code)
);

create table course(
    c_code varchar(8),
    title varchar(100),
    c_level varchar(8) check(c_level = 'beginner' or c_level = 'medium' or c_level = 'advanced'),
    description varchar(500),
    status varchar(6) check(status = 'expired' or status ='active'),
    retail_price numeric(8,2) check (retail_price > 0),
    primary key(c_code)
);

create table section(
    c_code varchar(8),
    sec_no varchar(8),
    year numeric(4,0) check(year > 1701 and year < 2100),
    complete_date date,
    offered_by varchar(20) check(offered_by = 'university' or offered_by = 'training company'),
    format varchar(16) check(format = 'classroom' or format = 'online-sync' or format = 'online-selfpaced' or format = 'correspondence'),
    primary key(c_code, sec_no, year),
    foreign key(c_code) references course on delete cascade
);

create table takes(
    per_id numeric(8,0),
    c_code varchar(8),
    sec_no varchar(8),
    year numeric(4,0) check(year > 1701 and year < 2100),
    primary key(per_id, sec_no, year),
    foreign key(per_id) references person on delete cascade,
    foreign key(c_code, sec_no, year) references section on delete cascade
);

create table required_cert(
    job_code varchar(8),
    cert_code varchar(20),
    primary key(job_code, cert_code),
    foreign key(job_code) references job on delete cascade,
    foreign key(cert_code) references certificate on delete cascade
);

create table has_cert(
    per_id numeric(8,0),
    cert_code varchar(20),
    primary key(per_id, cert_code),
    foreign key(per_id) references person on delete cascade,
    foreign key(cert_code) references certificate on delete cascade
);

create table grants_cert(
    c_code varchar(8),
    cert_code varchar(20),
    primary key(c_code, cert_code),
    foreign key(c_code) references course on delete cascade,
    foreign key(cert_code) references certificate on delete cascade
);

create table issued_by(
    comp_id varchar(8),
    cert_code varchar(20),
    primary key(comp_id, cert_code),
    foreign key(comp_id) references company on delete cascade,
    foreign key(cert_code) references certificate on delete cascade
);

create table has_prereq(
    c_code varchar(8),
    prereq_code varchar(20),
    primary key(c_code, prereq_code),
    foreign key(c_code) references course on delete cascade
);

create table has_category(
    cate_code varchar(8),
    job_code varchar(8),
    primary key(cate_code, job_code),
    foreign key(cate_code) references job_category on delete cascade,
    foreign key(job_code) references job on delete cascade
);

create table has_skill(
    ks_code varchar(8),
    per_id numeric(8,0),
    primary key(ks_code, per_id),
    foreign key(per_id) references person on delete cascade
);

create table teaches_skill(
    ks_code varchar(8),
    c_code varchar(8),
    primary key(ks_code, c_code),
    foreign key(c_code) references course on delete cascade
);

create table knowledge_skill(
    ks_code varchar(8),
    title varchar(100) not null,
    description varchar(500),
    ks_level varchar(8) check(ks_level = 'beginner' or ks_level = 'medium' or ks_level = 'advanced'),
    primary key(ks_code)
);
    
create table skill_set(
    ks_code varchar(8),
    cate_code varchar(8),
    primary key(ks_code, cate_code),
    foreign key(ks_code) references knowledge_skill on delete cascade,
    foreign key(cate_code) references job_category on delete cascade
);

create table required_skill(
    job_code varchar(8),
    ks_code varchar(8),
    primary key(job_code, ks_code),
    foreign key(job_code) references job on delete cascade,
    foreign key(ks_code) references knowledge_skill on delete cascade
);

create table preferred_skill(
    job_code varchar(8),
    ks_code varchar(8),
    primary key(job_code, ks_code),
    foreign key(job_code) references job on delete cascade,
    foreign key(ks_code) references knowledge_skill on delete cascade
);
