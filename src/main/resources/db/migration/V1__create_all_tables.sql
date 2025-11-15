-- V1__create_all_tables.sql

-- Tabela de Usuários
CREATE TABLE tb_usuarios (
    id VARCHAR2(255 CHAR) NOT NULL PRIMARY KEY,
    email VARCHAR2(255 CHAR) NOT NULL UNIQUE,
    full_name VARCHAR2(255 CHAR) NOT NULL,
    password VARCHAR2(255 CHAR) NOT NULL,
    user_role VARCHAR2(50 CHAR) DEFAULT 'USER' NOT NULL CHECK (user_role IN ('ADMIN', 'USER')),
    bio VARCHAR2(255 CHAR),
    avatar_url VARCHAR2(255 CHAR),
    time_credits NUMBER(10, 2) DEFAULT 10.0 NOT NULL,
    total_sessions_given NUMBER(10) DEFAULT 0,
    total_sessions_taken NUMBER(10) DEFAULT 0,
    average_rating NUMBER(3, 2) DEFAULT 0.0,
    location VARCHAR2(255 CHAR),
    timezone VARCHAR2(255 CHAR),
    linkedin_url VARCHAR2(255 CHAR),
    created_date TIMESTAMP,
    updated_date TIMESTAMP
);

-- Tabela de Habilidades
CREATE TABLE tb_habilidades (
    id VARCHAR2(255 CHAR) NOT NULL PRIMARY KEY,
    usuario_id VARCHAR2(255 CHAR) NOT NULL,
    name VARCHAR2(255 CHAR) NOT NULL,
    category VARCHAR2(50 CHAR) CHECK (category IN ('TECNOLOGIA', 'DESIGN', 'NEGOCIOS', 'IDIOMAS', 'MARKETING', 'DADOS', 'CRIATIVIDADE', 'SOFT_SKILLS', 'OUTROS')),
    description VARCHAR2(1000 CHAR),
    level VARCHAR2(50 CHAR) CHECK (level IN ('INICIANTE', 'INTERMEDIARIO', 'AVANCADO', 'EXPERT')),
    is_offering NUMBER(1) DEFAULT 1 NOT NULL,
    is_seeking NUMBER(1) DEFAULT 0 NOT NULL,
    hourly_rate NUMBER(10, 2),
    created_date TIMESTAMP,
    CONSTRAINT fk_habilidade_usuario FOREIGN KEY (usuario_id) REFERENCES tb_usuarios(id)
);

-- Tabela de Sessões
CREATE TABLE tb_sessoes (
    id VARCHAR2(255 CHAR) NOT NULL PRIMARY KEY,
    mentor_id VARCHAR2(255 CHAR) NOT NULL,
    mentorado_id VARCHAR2(255 CHAR) NOT NULL,
    habilidade_id VARCHAR2(255 CHAR) NOT NULL,
    skill_name VARCHAR2(255 CHAR),
    scheduled_date TIMESTAMP NOT NULL,
    duration_hours NUMBER(10, 2) DEFAULT 1.0 NOT NULL,
    status VARCHAR2(50 CHAR) DEFAULT 'AGENDADA' CHECK (status IN ('AGENDADA', 'CONFIRMADA', 'EM_ANDAMENTO', 'CONCLUIDA', 'CANCELADA')),
    meeting_link VARCHAR2(255 CHAR),
    notes VARCHAR2(2000 CHAR),
    credits_value NUMBER(10, 2),
    created_date TIMESTAMP,
    CONSTRAINT fk_sessao_mentor FOREIGN KEY (mentor_id) REFERENCES tb_usuarios(id),
    CONSTRAINT fk_sessao_mentorado FOREIGN KEY (mentorado_id) REFERENCES tb_usuarios(id),
    CONSTRAINT fk_sessao_habilidade FOREIGN KEY (habilidade_id) REFERENCES tb_habilidades(id)
);

-- Tabela de Transferências (Transações)
CREATE TABLE tb_transferencias (
    id VARCHAR2(255 CHAR) NOT NULL PRIMARY KEY,
    sessao_id VARCHAR2(255 CHAR),
    remetente_id VARCHAR2(255 CHAR) NOT NULL,
    destinatario_id VARCHAR2(255 CHAR) NOT NULL,
    credits NUMBER(10, 2) NOT NULL,
    type VARCHAR2(50 CHAR) CHECK (type IN ('PAGAMENTO_SESSAO', 'AJUSTE', 'BONUS_INICIAL', 'BONUS_REFERENCIA')),
    description VARCHAR2(255 CHAR),
    status VARCHAR2(50 CHAR) DEFAULT 'PENDENTE' CHECK (status IN ('PENDENTE', 'CONCLUIDA', 'ESTORNADA')),
    created_date TIMESTAMP,
    CONSTRAINT fk_transf_sessao FOREIGN KEY (sessao_id) REFERENCES tb_sessoes(id),
    CONSTRAINT fk_transf_remetente FOREIGN KEY (remetente_id) REFERENCES tb_usuarios(id),
    CONSTRAINT fk_transf_destinatario FOREIGN KEY (destinatario_id) REFERENCES tb_usuarios(id)
);

-- Tabela de Avaliações
CREATE TABLE tb_avaliacoes (
    id VARCHAR2(255 CHAR) NOT NULL PRIMARY KEY,
    sessao_id VARCHAR2(255 CHAR) NOT NULL,
    avaliador_id VARCHAR2(255 CHAR) NOT NULL,
    avaliado_id VARCHAR2(255 CHAR) NOT NULL,
    rating NUMBER(1) NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment VARCHAR2(2000 CHAR),
    created_date TIMESTAMP,
    CONSTRAINT fk_aval_sessao FOREIGN KEY (sessao_id) REFERENCES tb_sessoes(id),
    CONSTRAINT fk_aval_avaliador FOREIGN KEY (avaliador_id) REFERENCES tb_usuarios(id),
    CONSTRAINT fk_aval_avaliado FOREIGN KEY (avaliado_id) REFERENCES tb_usuarios(id)
);

-- Tabela de Email (do RabbitMQ)
CREATE TABLE tb_email (
    email_id RAW(16) NOT NULL PRIMARY KEY,
    owner_ref VARCHAR2(255 CHAR),
    email_from VARCHAR2(255 CHAR),
    email_to VARCHAR2(255 CHAR),
    subject VARCHAR2(255 CHAR),
    text CLOB,
    send_date_email TIMESTAMP,
    status_email VARCHAR2(50 CHAR) CHECK (status_email IN ('PROCESSING', 'SENT', 'ERROR'))
);