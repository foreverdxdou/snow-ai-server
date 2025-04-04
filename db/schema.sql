/*
 Navicat Premium Data Transfer

 Source Server         : NAS-PostgreSQL
 Source Server Type    : PostgreSQL
 Source Server Version : 140017
 Source Host           : 192.168.10.113:10036
 Source Catalog        : snow_ai
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 140017
 File Encoding         : 65001

 Date: 04/04/2025 20:41:37
*/


-- ----------------------------
-- Type structure for halfvec
-- ----------------------------
DROP TYPE IF EXISTS "public"."halfvec";
CREATE TYPE "public"."halfvec" (
  INPUT = "public"."halfvec_in",
  OUTPUT = "public"."halfvec_out",
  RECEIVE = "public"."halfvec_recv",
  SEND = "public"."halfvec_send",
  TYPMOD_IN = "public"."halfvec_typmod_in",
  INTERNALLENGTH = VARIABLE,
  STORAGE = external,
  CATEGORY = U,
  DELIMITER = ','
);
ALTER TYPE "public"."halfvec" OWNER TO "postgres";

-- ----------------------------
-- Type structure for sparsevec
-- ----------------------------
DROP TYPE IF EXISTS "public"."sparsevec";
CREATE TYPE "public"."sparsevec" (
  INPUT = "public"."sparsevec_in",
  OUTPUT = "public"."sparsevec_out",
  RECEIVE = "public"."sparsevec_recv",
  SEND = "public"."sparsevec_send",
  TYPMOD_IN = "public"."sparsevec_typmod_in",
  INTERNALLENGTH = VARIABLE,
  STORAGE = external,
  CATEGORY = U,
  DELIMITER = ','
);
ALTER TYPE "public"."sparsevec" OWNER TO "postgres";

-- ----------------------------
-- Type structure for vector
-- ----------------------------
DROP TYPE IF EXISTS "public"."vector";
CREATE TYPE "public"."vector" (
  INPUT = "public"."vector_in",
  OUTPUT = "public"."vector_out",
  RECEIVE = "public"."vector_recv",
  SEND = "public"."vector_send",
  TYPMOD_IN = "public"."vector_typmod_in",
  INTERNALLENGTH = VARIABLE,
  STORAGE = external,
  CATEGORY = U,
  DELIMITER = ','
);
ALTER TYPE "public"."vector" OWNER TO "postgres";

-- ----------------------------
-- Sequence structure for embedding_config_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."embedding_config_id_seq";
CREATE SEQUENCE "public"."embedding_config_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for kb_category_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."kb_category_id_seq";
CREATE SEQUENCE "public"."kb_category_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for kb_chat_history_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."kb_chat_history_id_seq";
CREATE SEQUENCE "public"."kb_chat_history_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for kb_document_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."kb_document_id_seq";
CREATE SEQUENCE "public"."kb_document_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for kb_document_tag_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."kb_document_tag_id_seq";
CREATE SEQUENCE "public"."kb_document_tag_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for kb_document_vector_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."kb_document_vector_id_seq";
CREATE SEQUENCE "public"."kb_document_vector_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for kb_document_version_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."kb_document_version_id_seq";
CREATE SEQUENCE "public"."kb_document_version_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for kb_knowledge_base_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."kb_knowledge_base_id_seq";
CREATE SEQUENCE "public"."kb_knowledge_base_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for kb_knowledge_base_permission_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."kb_knowledge_base_permission_id_seq";
CREATE SEQUENCE "public"."kb_knowledge_base_permission_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for kb_tag_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."kb_tag_id_seq";
CREATE SEQUENCE "public"."kb_tag_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for kb_user_behavior_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."kb_user_behavior_id_seq";
CREATE SEQUENCE "public"."kb_user_behavior_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for llm_config_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."llm_config_id_seq";
CREATE SEQUENCE "public"."llm_config_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for llm_token_usage_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."llm_token_usage_id_seq";
CREATE SEQUENCE "public"."llm_token_usage_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sys_ai_model_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_ai_model_id_seq";
CREATE SEQUENCE "public"."sys_ai_model_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sys_dept_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_dept_id_seq";
CREATE SEQUENCE "public"."sys_dept_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sys_permission_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_permission_id_seq";
CREATE SEQUENCE "public"."sys_permission_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sys_role_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_role_id_seq";
CREATE SEQUENCE "public"."sys_role_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sys_role_permission_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_role_permission_id_seq";
CREATE SEQUENCE "public"."sys_role_permission_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sys_user_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_user_id_seq";
CREATE SEQUENCE "public"."sys_user_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sys_user_role_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_user_role_id_seq";
CREATE SEQUENCE "public"."sys_user_role_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for system_config_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."system_config_id_seq";
CREATE SEQUENCE "public"."system_config_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Table structure for embedding_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."embedding_config";
CREATE TABLE "public"."embedding_config" (
  "id" int8 NOT NULL DEFAULT nextval('embedding_config_id_seq'::regclass),
  "name" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "model_type" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "api_key" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "base_url" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "dimensions" int4 NOT NULL DEFAULT 2048,
  "create_time" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "update_time" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "creator_id" int8 NOT NULL,
  "remark" varchar(500) COLLATE "pg_catalog"."default",
  "updater_id" int8,
  "status" int4
)
;
COMMENT ON COLUMN "public"."embedding_config"."id" IS '主键ID';
COMMENT ON COLUMN "public"."embedding_config"."name" IS '模型名称';
COMMENT ON COLUMN "public"."embedding_config"."model_type" IS '模型类型';
COMMENT ON COLUMN "public"."embedding_config"."api_key" IS 'API密钥';
COMMENT ON COLUMN "public"."embedding_config"."base_url" IS 'API基础URL';
COMMENT ON COLUMN "public"."embedding_config"."dimensions" IS '模型维度';
COMMENT ON COLUMN "public"."embedding_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."embedding_config"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."embedding_config"."creator_id" IS '创建者ID';
COMMENT ON COLUMN "public"."embedding_config"."remark" IS '备注';
COMMENT ON COLUMN "public"."embedding_config"."updater_id" IS '更新者ID';
COMMENT ON TABLE "public"."embedding_config" IS 'Embedding模型配置表';

-- ----------------------------
-- Records of embedding_config
-- ----------------------------
INSERT INTO "public"."embedding_config" VALUES (15, 'bge-m3', 'bge-m3', '111', 'http://localhost:11434/api/embed', 1536, '2025-04-02 20:49:40.047289', '2025-04-02 21:17:33.722949', 1, 'https://github.com/ollama/ollama/blob/main/docs/api.md', 1, 1);

-- ----------------------------
-- Table structure for kb_category
-- ----------------------------
DROP TABLE IF EXISTS "public"."kb_category";
CREATE TABLE "public"."kb_category" (
  "id" int8 NOT NULL DEFAULT nextval('kb_category_id_seq'::regclass),
  "name" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "description" text COLLATE "pg_catalog"."default",
  "parent_id" int8 DEFAULT 0,
  "kb_id" int8,
  "sort" int4 DEFAULT 0,
  "creator_id" int8 NOT NULL,
  "status" int4 DEFAULT 1,
  "create_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
  "update_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
  "deleted" int4 DEFAULT 0
)
;

-- ----------------------------
-- Records of kb_category
-- ----------------------------
INSERT INTO "public"."kb_category" VALUES (17, '学术类', '论文、研究成果、学科知识（如 Google Scholar、ResearchGate）。', 0, 1, 0, 1, 1, '2025-04-04 08:43:36.519854', '2025-04-04 08:43:36.519854', 0);
INSERT INTO "public"."kb_category" VALUES (18, '生活类', '日常问题解答、DIY 技巧、健康建议（如 WikiHow、Quora）。
', 0, 1, 0, 1, 1, '2025-04-04 08:43:36.519854', '2025-04-04 08:43:36.519854', 0);
INSERT INTO "public"."kb_category" VALUES (19, 'FAQ 知识库', '常见问题解答（如公司客服支持的知识库）', 0, 1, 0, 1, 1, '2025-04-04 08:43:36.519854', '2025-04-04 08:43:36.519854', 0);
INSERT INTO "public"."kb_category" VALUES (20, '文档型知识库', '产品文档、API 文档、用户手册（如 Microsoft Docs、MDN Web Docs）', 0, 1, 0, 1, 1, '2025-04-04 08:43:36.519854', '2025-04-04 08:43:36.519854', 0);
INSERT INTO "public"."kb_category" VALUES (21, '公开知识库', '对所有人开放，任何人都可以访问和贡献内容（如 Wikipedia、Stack Overflow）', 0, 1, 0, 1, 1, '2025-04-04 08:43:36.519854', '2025-04-04 08:43:36.519854', 0);
INSERT INTO "public"."kb_category" VALUES (22, '私有知识库', '仅限特定用户或组织内部使用（如公司内部文档系统）', 0, 1, 0, 1, 1, '2025-04-04 08:43:36.519854', '2025-04-04 08:43:36.519854', 0);
INSERT INTO "public"."kb_category" VALUES (23, '混合知识库', '部分内容公开，部分内容需要权限访问（如付费课程平台）', 0, 1, 0, 1, 1, '2025-04-04 08:43:36.519854', '2025-04-04 08:43:36.519854', 0);
INSERT INTO "public"."kb_category" VALUES (1, '教育类', '学习资源、课程、教程（如 Khan Academy、Coursera）', 0, 1, 0, 1, 1, '2025-03-24 23:12:07.018269', '2025-03-31 18:05:55.933666', 0);
INSERT INTO "public"."kb_category" VALUES (3, '技术类', '编程语言、开发工具、框架、算法等（如 Stack Overflow、GitHub Docs）', 0, 1, 0, 1, 1, '2025-04-04 08:43:36.519854', '2025-04-04 08:43:36.519854', 0);
INSERT INTO "public"."kb_category" VALUES (2, '学习资源', '2', 1, 1, 1, 1, 1, '2025-03-24 23:07:26.185522', '2025-04-04 18:52:53.762457', 0);

-- ----------------------------
-- Table structure for kb_chat_history
-- ----------------------------
DROP TABLE IF EXISTS "public"."kb_chat_history";
CREATE TABLE "public"."kb_chat_history" (
  "id" int8 NOT NULL DEFAULT nextval('kb_chat_history_id_seq'::regclass),
  "session_id" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "kb_ids" varchar(255) COLLATE "pg_catalog"."default",
  "user_id" int8 NOT NULL,
  "question" text COLLATE "pg_catalog"."default" NOT NULL,
  "answer" text COLLATE "pg_catalog"."default" NOT NULL,
  "tokens_used" int4,
  "process_time" int8,
  "create_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
  "update_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP
)
;

-- ----------------------------
-- Records of kb_chat_history
-- ----------------------------
INSERT INTO "public"."kb_chat_history" VALUES (166, '98d9a5e3-12f0-43f1-acb0-f360ff9ea52d', NULL, 1, '人生的意义', '<think>嗯，用户问的是“人生的意义”。这个问题好大啊，我得仔细想想怎么回答。首先，用户可能是在寻找一些哲学上的见解，或者他们可能在经历一些困惑，想得到一些指引。可能他们正处于人生的转折点，比如刚毕业、换工作，或者遇到了挫折，需要找到方向。接下来，我得考虑不同的哲学观点。存在主义认为人生没有预设的意义，需要自己创造。这可能适合那些想要自主权的人。宗教观点的话，比如基督教、佛教，可能会提供不同的答案，但用户没有提到信仰，所以可能需要保持中立。

还有，用户可能希望得到实际一点的建议，而不仅仅是理论。比如，寻找个人目标、人际关系、贡献社会这些方面。要避免过于抽象，同时也要避免武断，毕竟每个人情况不同。

另外，得注意语气。用户可能希望被理解，而不是被说教。所以回答应该温和，鼓励他们自己探索，而不是给出一个标准答案。还要考虑到不同文化背景，比如东方哲学中的和谐、平衡，或者西方个人主义的追求。

可能还需要提到心理学角度，比如积极心理学中的幸福感和成就感来源。或者提到找到意义的过程比答案本身更重要，强调探索的旅程。

还要检查有没有潜在的需求，比如用户是否感到迷茫，是否需要资源或进一步的帮助。但问题本身比较抽象，可能不需要深入到这里，保持回答的哲学性比较合适。

最后，结构要清晰，分点列出不同视角，让用户能从中找到共鸣的点。同时结尾要给予积极的鼓励，让他们觉得自己的探索是有价值的。</think>关于"人生的意义"这个永恒的问题，不同文化、哲学体系和个体都有独特的理解。以下从多个视角为您提供思考路径：

1. **存在主义视角**
萨特认为"存在先于本质"，人生本无预设意义，正因如此人类才获得自由——我们可以通过选择与行动赋予生命意义。加缪在《西西弗斯神话》中更指出，在认识到世界的荒诞性后依然选择热爱生活，本身就是对抗虚无的英雄主义。

2. **东方哲学智慧**
儒家强调"修身齐家治国平天下"的入世担当，道家追求"天人合一"的自然和谐，佛家通过破除"我执"寻求解脱。禅宗公案常以"吃茶去""担水砍柴"启示：意义不在远方，而在当下的体悟。

3. **科学演化视角**
生物学角度而言，生命是数十亿年演化进程的延续。理查德·道金斯在《自私的基因》中指出，我们既是基因传承的载体，也因独特的意识能力而超越本能，创造文化意义上的不朽。

4. **心理学研究启示**
维克多·弗兰克尔在集中营经历中发展出"意义疗法"：人类最基本的动力不是快乐追求，而是意义寻找。他发现人可以通过创造价值（工作）、体验爱（关系）以及超越苦难（态度选择）三个途径找到生命意义。

5. **日常实践路径-建立深度关系：神经科学证实，与他人产生情感共鸣时大脑会分泌催产素-心流体验：专注投入某个超越自我的目标时，时间感会消失-服务他人：哈佛75年追踪研究显示，良好人际关系是幸福的最强预测因子最终答案可能需要用一生来探索。正如诗人里尔克所说："耐心对待所有未解之谜，试着去爱问题本身。"或许真正的意义不在于某个具体答案，而在于保持追问的勇气，在有限中创造无限，在短暂中触及永恒。您此刻的思考本身，已然是意义生成的重要部分。', 100, 41294, '2025-04-03 23:03:05.902381', '2025-04-03 23:03:05.902381');
INSERT INTO "public"."kb_chat_history" VALUES (165, '98d9a5e3-12f0-43f1-acb0-f360ff9ea52d', NULL, 1, '人生的意义', '<think>嗯，用户问的是“人生的意义”，这个问题好大啊，我得仔细想想怎么回答。首先，我得考虑用户为什么会问这个问题呢？可能是正在寻找生活的方向，或者遇到了困惑，想得到一些启发。可能他们的年龄层也不一样，如果是年轻人，可能是在探索自我；如果是中年人，可能在经历一些转折点。接下来，我需要回顾一下哲学、心理学等领域对人生意义的常见观点。比如存在主义认为意义是自己创造的，宗教可能有预设的意义，还有功利主义强调对他人的贡献。然后还要考虑用户可能的文化背景，如果是中文用户，可能涉及到儒家思想，比如修身齐家治国平天下，或者道家的顺应自然。

还要注意回答不能太学术化，要通俗易懂，同时保持深度。用户可能希望得到不同角度的见解，而不是单一的答案。所以应该列举几种不同的观点，并说明没有统一答案，鼓励他们自己探索。

另外，要避免给出绝对的答案，因为人生的意义因人而异。可能需要强调自我反思、设定目标、人际关系的重要性，以及寻找过程中的成长。还要注意语气要温暖、鼓励，让用户感到被理解和支持。

有没有可能用户有潜在的需求？比如他们可能感到迷茫，需要一些实际的建议。所以除了理论上的观点，可以加入一些行动上的建议，比如自我探索、帮助他人、追求热情等。

还要注意避免过于抽象，用具体的例子来说明，比如提到追求知识、艺术创作、家庭幸福等不同的生活目标。这样用户更容易联想到自己的情况。

最后，总结时要强调这是一个持续的过程，答案可能会随时间改变，重要的是主动去探索和体验。这样既尊重了用户的个人经历，又给予了积极的引导。</think>关于“人生的意义”，这是一个哲学、宗教、心理学等多个领域持续探讨的终极问题，不同文化和思想体系可能给出不同的答案。以下是一些常见的视角，供你参考和思考：

---

###1. **存在主义视角：意义由自己创造**存在主义哲学家（如萨特、加缪）认为，人生本无预设的意义，但正因为如此，人类拥有自由去**赋予**生命意义。→ **行动建议**：通过选择、承担责任、追求热爱之事（如艺术、科学、帮助他人），在行动中创造属于自己的价值。

---

###2. **宗教与灵性视角：超越性的目的**许多宗教认为人生意义与更高存在或宇宙法则相关，例如：-佛教：通过修行脱离苦海，达到觉悟（涅槃）。-基督教：活出爱与奉献，与神建立关系。-道家：顺应自然之道，追求天人合一。---

###3. **生物学视角：延续与演化**从进化论角度看，生命的意义可能在于基因的延续和物种的生存。但人类独有的自我意识让我们超越这一本能，寻求更深层的价值。

---

###4. **心理学视角：幸福与联结**积极心理学（如马丁·塞利格曼）提出，人生的意义可能与以下要素相关：- **积极情绪**：体验快乐、满足感。- **投入**：专注于有意义的活动（心流状态）。- **人际关系**：与所爱之人建立深刻联结。- **意义感**：归属并服务于比自我更大的目标。---

###5. **虚无主义视角：无意义中的自由**如果承认人生本质无意义，反而可能获得解脱——不必被外界的标准束缚，可以更自由地选择生活方式，如加缪笔下的西西弗斯“在反抗荒谬中寻找幸福”。

---

### **如何寻找属于自己的答案？**- **向内探索**：通过冥想、写作、艺术表达等方式反思自己的价值观与渴望。- **向外实践**：尝试帮助他人、学习新领域、接触不同文化，在行动中感受意义。- **接受动态性**：人生的意义可能随时间、经历而改变，无需固守单一答案。

---

###最后或许，人生的意义不在于找到一个标准答案，而在于**持续追问的过程**——如同诗人里尔克所说：“活在问题本身之中。”你的每一次选择、感受与成长，都在为这个问题增添独特的注解。', 100, 47591, '2025-04-03 22:58:45.562281', '2025-04-03 22:58:45.562281');
INSERT INTO "public"."kb_chat_history" VALUES (164, '98d9a5e3-12f0-43f1-acb0-f360ff9ea52d', NULL, 1, '你好', '<think>好的，用户发来了一句“你好”，我需要用中文回应。首先，应该保持友好和礼貌。然后，确认他们是否有具体的问题需要帮助。可能他们只是想打个招呼，或者接下来会有其他问题。应该用简洁明了的语言，避免使用复杂的句子。同时，要表现出愿意提供帮助的态度。比如可以回复：“你好！有什么我可以帮助你的吗？”这样既回应了问候，又引导用户进一步说明需求。另外，注意使用正确的标点符号和表情符号（如果有的话），但这里可能不需要表情。检查一下有没有拼写错误，确保回复准确。最后，保持自然，不要显得机械。这样应该可以满足用户的需求了。</think>你好！有什么我可以帮助你的吗？', 100, 12221, '2025-04-03 22:57:42.825544', '2025-04-03 22:57:42.825544');
INSERT INTO "public"."kb_chat_history" VALUES (163, '98d9a5e3-12f0-43f1-acb0-f360ff9ea52d', NULL, 1, '你好', '<think>嗯，用户发来一个“你好”，这应该是在打招呼吧。首先，我需要确认用户的需求是什么。可能只是想测试聊天机器人，或者真的有事情要询问。这种情况下，我应该保持友好，回应问候，并邀请他们提出具体的问题。同时，要注意语气亲切，避免太机械。可能需要用中文回复，因为用户用的是中文。另外，用户可能还有其他问题没有说出来，所以应该鼓励他们进一步交流。检查有没有拼写错误，确保回复正确。最后，保持简洁明了，不让用户等待太久。好的，回应应该类似于“你好！有什么我可以帮助你的吗？”这样既礼貌又开放，促进进一步对话。</think>你好！有什么我可以帮助你的吗？', 100, 10967, '2025-04-03 22:56:06.728296', '2025-04-03 22:56:06.728296');
INSERT INTO "public"."kb_chat_history" VALUES (162, '98d9a5e3-12f0-43f1-acb0-f360ff9ea52d', NULL, 1, '你好', '<think>好的，用户发来了“你好”，这是一个常见的中文问候。我需要用中文回应，保持友好和自然。首先，应该回复“你好！有什么我可以帮助你的吗？”这样既礼貌又开放，邀请用户提出他们的需求。同时，要注意用简洁的句子，避免复杂结构。检查有没有拼写错误，确保语气亲切。用户可能只是想打个招呼，或者接下来会有具体的问题，所以回答要准备好进一步协助。保持回答的专业性，同时带点温暖的感觉，让用户感到被欢迎。不需要使用表情符号或其他非文字元素，符合当前平台的要求。确认所有内容符合指导方针，没有涉及敏感话题。现在，生成回复。</think>你好！有什么我可以帮助你的吗？', 100, 11247, '2025-04-03 22:52:24.883037', '2025-04-03 22:52:24.883037');
INSERT INTO "public"."kb_chat_history" VALUES (161, '98d9a5e3-12f0-43f1-acb0-f360ff9ea52d', NULL, 1, '你好', '<think>好的，我现在要处理用户的问题：“请回答以下问题：你好”。首先，我需要理解用户的需求。用户发送了一个“你好”，这看起来像是一个简单的问候，但可能隐藏着更深层次的请求。接下来，我需要检查用户提供的示例对话，看看是否有类似的模式。在示例中，用户问“你好”，然后助手用中文回应了问候，并询问有什么可以帮助的。这说明用户可能希望得到一个礼貌且友好的回应，同时保持开放，以便进一步交流。因此，我需要确保我的回答符合这个模式：先回应问候，然后主动提供帮助。

接下来，我要确认是否存在任何潜在的隐藏需求。例如，用户是否在测试我的响应能力，或者他们是否在检查我是否能够处理简单的问候。此外，我需要确保回答符合所有给定的规则，比如使用中文、保持口语化、避免使用Markdown格式，以及保持回答的简洁性。

然后，我需要考虑是否有其他可能的回答方式，但根据示例，正确的做法是直接回应用户的问候，并邀请他们提出更多问题。因此，我的回答应该类似于示例中的结构，但需要避免重复完全相同的话语，以保持自然。

最后，我要确保回答没有格式错误，并且完全符合用户的要求。检查是否有任何技术问题或误解，确保回答准确且恰当。</think>你好！有什么我可以帮你的吗？', 100, 20674, '2025-04-03 22:43:04.020901', '2025-04-03 22:43:04.020901');
INSERT INTO "public"."kb_chat_history" VALUES (159, '98d9a5e3-12f0-43f1-acb0-f360ff9ea52d', NULL, 1, '你好', '<think>好的，用户发来了一条消息：“你好”。首先，我需要确认这是一个中文的问候，所以应该用中文回应。用户可能只是打个招呼，或者在测试我的反应。接下来，我应该保持友好和亲切的语气，回复一个合适的问候，比如“你好！有什么可以帮助你的吗？”这样既回应了问候，又主动提供帮助，引导用户进一步说明需求。同时，要确保回复简洁，符合用户可能的期望。检查有没有其他可能的意图，但当前信息有限，保持通用回复比较合适。最后确认回复是否符合社区指引，确保内容合适。</think>你好！有什么可以帮助你的吗？', 100, 10534, '2025-04-03 22:37:28.797853', '2025-04-03 22:37:28.797853');
INSERT INTO "public"."kb_chat_history" VALUES (160, '98d9a5e3-12f0-43f1-acb0-f360ff9ea52d', NULL, 1, '你好', '<think>嗯，用户发来的是“你好”，看起来是中文里常见的问候。我需要用中文回应，保持友好和自然。首先，确认用户的需求，可能只是打个招呼或者有后续问题。所以，我的回答应该既回应问候，又邀请他们提出具体问题。要注意语气亲切，避免过于机械。比如，可以说“你好！有什么我可以帮助你的吗？”这样既礼貌又开放，鼓励用户进一步交流。另外，要检查有没有拼写错误，确保回复正确无误。可能用户接下来会问问题，所以保持回答简洁，不冗长。同时，符合中文的表达习惯，使用常见的问候方式，让用户感到舒适。最后，确认回复符合所有设定的规则，比如不涉及敏感内容，保持积极正面。这样处理应该能有效回应用户的初始问候，并促进进一步的互动。</think>你好！有什么我可以帮助你的吗？', 100, 16325, '2025-04-03 22:41:07.85262', '2025-04-03 22:41:07.85262');
INSERT INTO "public"."kb_chat_history" VALUES (158, '98d9a5e3-12f0-43f1-acb0-f360ff9ea52d', NULL, 1, '你好', '<think>好的，用户发来了一个简单的“你好”，我需要用中文回应。首先，我要保持友好和亲切的态度。用户可能是在测试我的回应，或者只是想打个招呼。接下来，我应该给出一个热情的回复，同时提供进一步的帮助。可能需要用一些表情符号让对话更生动。然后，考虑用户可能需要什么帮助，比如问题、聊天或者信息查询。确保回复简洁明了，没有使用复杂句子。最后，检查有没有语法错误，确保表达自然流畅。现在，组合这些元素形成一个合适的回答。</think>你好！很高兴见到你，有什么我可以帮忙的吗？无论是问题、聊天还是其他需求，我都在这里哦～ 😊', 100, 11162, '2025-04-03 22:35:11.960548', '2025-04-03 22:35:11.960548');
INSERT INTO "public"."kb_chat_history" VALUES (157, '98d9a5e3-12f0-43f1-acb0-f360ff9ea52d', NULL, 1, '你好', '<think>嗯，用户发来了一句“你好”，看起来是在用中文打招呼。我需要用中文回应，保持友好和自然。首先，确认用户的需求可能只是简单的问候，或者他们可能有后续的问题。应该先礼貌回应，然后主动提供帮助，让用户知道可以继续提问。要注意语气亲切，避免过于机械。比如用“有什么我可以帮您的吗？”来邀请他们进一步交流。同时，保持回答简洁，不需要太长的内容。确保符合格式要求，不使用任何Markdown，只用纯文本。另外，检查是否有拼写错误或语法问题，确保回答正确无误。最后，确认回应用户的中文是否恰当，避免翻译上的问题。总结下来，回应应该简单直接，友好，同时开放式的提问以促进进一步沟通。</think>你好！很高兴见到您，有什么我可以帮您的吗？', 100, 12557, '2025-04-03 22:23:14.612676', '2025-04-03 22:23:14.612676');

-- ----------------------------
-- Table structure for kb_document
-- ----------------------------
DROP TABLE IF EXISTS "public"."kb_document";
CREATE TABLE "public"."kb_document" (
  "id" int8 NOT NULL DEFAULT nextval('kb_document_id_seq'::regclass),
  "title" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
  "content" text COLLATE "pg_catalog"."default",
  "file_type" varchar(128) COLLATE "pg_catalog"."default",
  "file_size" int8,
  "file_url" varchar(500) COLLATE "pg_catalog"."default",
  "category_id" int8,
  "kb_id" int8 NOT NULL,
  "version" int4 DEFAULT 1,
  "status" int4 DEFAULT 1,
  "creator_id" int8 NOT NULL,
  "create_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
  "update_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
  "deleted" int4 DEFAULT 0,
  "parse_status" int4 DEFAULT 0,
  "parse_error" text COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."kb_document"."parse_status" IS '解析状态（0：未解析，1：解析中，2：解析成功，3：解析失败）';
COMMENT ON COLUMN "public"."kb_document"."parse_error" IS '解析失败原因';

-- ----------------------------
-- Records of kb_document
-- ----------------------------
INSERT INTO "public"."kb_document" VALUES (44, 'Python编程从入门到实践：核心概念与实战指南.md', '# Python编程从入门到实践：核心概念与实战指南

## 目录
1. Python语言概述
2. 开发环境搭建
3. 基础语法详解
4. 面向对象编程
5. 异常处理机制
6. 文件操作与持久化
7. 实战项目：Web应用开发
8. 调试与测试
9. 性能优化技巧
10. 部署与维护

---

## 1. Python语言概述
### 1.1 Python的特点
- 解释型语言，无需编译
- 动态类型系统
- 丰富的标准库和第三方库
- 跨平台运行能力

### 1.2 应用领域
- Web开发（Django/Flask）
- 数据科学（Pandas/Numpy）
- 人工智能（TensorFlow/PyTorch）
- 自动化脚本
- 网络爬虫

---

## 2. 开发环境搭建
### 2.1 安装Python
```bash
# Ubuntu系统
sudo apt update
sudo apt install python3

# macOS系统
brew install python

# Windows系统
下载安装包：https://www.python.org/downloads/
```

### 2.2 虚拟环境配置
```python
# 创建虚拟环境
python3 -m venv myenv

# 激活环境
# Linux/macOS
source myenv/bin/activate
# Windows
myenv\Scripts\activate
```

### 2.3 IDE选择建议
- PyCharm（专业版/社区版）
- VS Code（安装Python插件）
- Jupyter Notebook（数据科学首选）

---

## 3. 基础语法详解
### 3.1 变量与数据类型
```python
# 基本类型示例
age = 25                # 整型
price = 19.99           # 浮点型
name = "Alice"          # 字符串
is_student = True       # 布尔型
complex_num = 3 + 4j    # 复数

# 容器类型
fruits = ["apple", "banana", "cherry"]  # 列表
person = {"name": "Bob", "age": 30}     # 字典
unique_numbers = {1, 2, 3, 3}           # 集合
```

### 3.2 控制结构
```python
# 条件语句
score = 85
if score >= 90:
    print("A")
elif score >= 80:
    print("B")
else:
    print("C")

# 循环结构
# for循环
for i in range(5):
    print(i)

# while循环
count = 0
while count < 5:
    print(count)
    count += 1
```

### 3.3 函数定义
```python
def calculate_area(radius):
    """计算圆面积"""
    return 3.14159 * radius ** 2

# 使用lambda表达式
square = lambda x: x * x
print(square(5))  # 输出25
```

### 3.4 列表推导式
```python
# 生成平方数列表
squares = [x**2 for x in range(10)]
# 带条件过滤
even_squares = [x**2 for x in range(10) if x % 2 == 0]
```

---

## 4. 面向对象编程
### 4.1 类定义
```python
class Vehicle:
    def __init__(self, brand, model):
        self.brand = brand
        self.model = model

    def display_info(self):
        return f"{self.brand} {self.model}"

class Car(Vehicle):
    def __init__(self, brand, model, num_doors):
        super().__init__(brand, model)
        self.num_doors = num_doors

    def display_info(self):
        return f"{super().display_info()} with {self.num_doors} doors"
```

### 4.2 魔术方法
```python
class Point:
    def __init__(self, x, y):
        self.x = x
        self.y = y

    def __str__(self):
        return f"Point({self.x}, {self.y})"

    def __add__(self, other):
        return Point(self.x + other.x, self.y + other.y)
```

### 4.3 类方法与静态方法
```python
class MathUtils:
    @staticmethod
    def add(a, b):
        return a + b

    @classmethod
    def from_string(cls, string):
        x, y = map(int, string.split('',''))
        return cls(x, y)
```

---

## 5. 异常处理机制
```python
try:
    result = 10 / 0
except ZeroDivisionError as e:
    print(f"Error: {e}")
finally:
    print("Execution completed")

# 自定义异常
class CustomError(Exception):
    def __init__(self, message):
        self.message = message
```

---

## 6. 文件操作与持久化
### 6.1 基本文件操作
```python
# 写入文件
with open(''data.txt'', ''w'') as f:
    f.write("Hello World")

# 读取文件
with open(''data.txt'', ''r'') as f:
    content = f.read()
```

### 6.2 使用Pickle序列化
```python
import pickle

data = {''name'': ''Alice'', ''age'': 30}
with open(''data.pkl'', ''wb'') as f:
    pickle.dump(data, f)

with open(''data.pkl'', ''rb'') as f:
    loaded_data = pickle.load(f)
```

### 6.3 CSV文件处理
```python
import csv

# 写入CSV
with open(''data.csv'', ''w'', newline='''') as f:
    writer = csv.writer(f)
    writer.writerow([''Name'', ''Age''])
    writer.writerow([''Alice'', 30])

# 读取CSV
with open(''data.csv'', ''r'') as f:
    reader = csv.DictReader(f)
    for row in reader:
        print(row[''Name''], row[''Age''])
```

---

## 7. 实战项目：Web应用开发
### 7.1 使用Flask框架
```python
from flask import Flask, render_template, request

app = Flask(__name__)

@app.route(''/'')
def index():
    return "Hello, World!"

@app.route(''/greet/<name>'')
def greet(name):
    return f"Hello, {name}!"

if __name__ == ''__main__'':
    app.run(debug=True)
```

### 7.2 数据库集成
```python
from flask_sqlalchemy import SQLAlchemy

app.config[''SQLALCHEMY_DATABASE_URI''] = ''sqlite:///site.db''
db = SQLAlchemy(app)

class User(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    username = db.Column(db.String(20), unique=True, nullable=False)
    email = db.Column(db.String(120), unique=True, nullable=False)

    def __repr__(self):
        return f"User(''{self.username}'', ''{self.email}'')"
```

---

## 8. 调试与测试
### 8.1 使用pdb调试
```python
import pdb

def calculate_sum(a, b):
    pdb.set_trace()
    return a + b

calculate_sum(3, 5)
```

### 8.2 单元测试
```python
import unittest

class TestMathOperations(unittest.TestCase):
    def test_add(self):
        self.assertEqual(1 + 1, 2)

    def test_subtract(self):
        self.assertEqual(3 - 1, 2)

if __name__ == ''__main__'':
    unittest.main()
```

---

## 9. 性能优化技巧
### 9.1 算法优化
```python
# 低效实现
def sum_n(n):
    total = 0
    for i in range(n+1):
        total += i
    return total

# 高效实现
def sum_n(n):
    return n * (n + 1) // 2
```

### 9.2 内存管理
```python
# 使用生成器节省内存
def read_large_file(file_path):
    with open(file_path, ''r'') as f:
        for line in f:
            yield line
```

### 9.3 并行计算
```python
from multiprocessing import Pool

def process_data(data):
    # 数据处理逻辑
    return processed_data

if __name__ == ''__main__'':
    with Pool(4) as p:
        results = p.map(process_data, large_dataset)
```

---

## 10. 部署与维护
### 10.1 使用Docker容器化
```dockerfile
# Dockerfile示例
FROM python:3.9-slim
WORKDIR /app
COPY requirements.txt .
RUN pip install -r requirements.txt
COPY . .
CMD ["python", "app.py"]
```

### 10.2 持续集成配置
```yaml
# GitHub Actions示例
name: Python CI

on: [push]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v2
    - name: Set up Python
      uses: actions/setup-python@v2
      with:
        python-version: 3.9
    - name: Install dependencies
      run: |
        python -m pip install --upgrade pip
        pip install -r requirements.txt
    - name: Test
      run: pytest
```

---

', 'application/octet-stream', 7267, '/snow-ai/5f6abb1a20bd44ac8d1d9834eda50d85.md', NULL, 32, 1, 1, 1, '2025-04-04 17:11:14.278697', '2025-04-04 17:18:26.853283', 0, 2, NULL);

-- ----------------------------
-- Table structure for kb_document_tag
-- ----------------------------
DROP TABLE IF EXISTS "public"."kb_document_tag";
CREATE TABLE "public"."kb_document_tag" (
  "id" int8 NOT NULL DEFAULT nextval('kb_document_tag_id_seq'::regclass),
  "document_id" int8 NOT NULL,
  "tag_id" int8 NOT NULL,
  "creator_id" int8,
  "create_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
  "deleted" int4 DEFAULT 0
)
;

-- ----------------------------
-- Records of kb_document_tag
-- ----------------------------
INSERT INTO "public"."kb_document_tag" VALUES (8, 44, 23, NULL, '2025-04-04 17:11:14.278697', 0);

-- ----------------------------
-- Table structure for kb_document_vector
-- ----------------------------
DROP TABLE IF EXISTS "public"."kb_document_vector";
CREATE TABLE "public"."kb_document_vector" (
  "id" int8 NOT NULL DEFAULT nextval('kb_document_vector_id_seq'::regclass),
  "document_id" int8 NOT NULL,
  "content_vector" "public"."vector",
  "chunk_index" int4,
  "chunk_content" text COLLATE "pg_catalog"."default",
  "similarity" float8,
  "create_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP
)
;

-- ----------------------------
-- Records of kb_document_vector
-- ----------------------------
INSERT INTO "public"."kb_document_vector" VALUES (75, 44, '[0.0010226868,0.017716149,-0.016853867,0.017471794,0.008114945,-0.019787192,0.07739784,0.05654671,-0.020766806,-0.004730875,0.0030609015,0.027317388,-0.018341338,0.026239049,-0.024766114,-0.028474389,-0.035839494,0.01691237,-0.008972281,-0.0012857844,-0.02699377,-0.0059821173,0.026423918,-0.007955839,-0.03035929,0.07632248,-0.007438516,-0.024240984,-0.0309862,0.0007920084,0.028516654,-0.01213177,0.017787505,-0.0045741233,0.005333657,-0.040177554,-0.011140362,-0.017714808,-0.11093111,-0.013836731,0.038211107,-0.005828318,-0.052001692,0.004269068,0.02273809,0.008156273,-0.032227796,-0.057333734,-0.035160653,-0.008533406,0.00012715567,-0.01021888,0.00030060275,-0.039361767,0.006076061,0.030391322,0.0016285329,-0.0024674793,-0.010978641,-0.017831706,-0.026878962,0.01521214,-0.0196036,-0.022985073,-0.029240206,0.024721153,0.006229195,0.042443812,0.02034671,-0.031410858,-0.015423158,-0.005526733,-0.02387943,-0.053170823,-0.007756025,-0.010753129,0.025740588,-0.00035924514,-0.008251521,-0.07387626,0.11486679,-0.040472068,0.027583301,-0.019486422,-0.026914,-0.025670072,0.002643484,0.041606702,0.0066470513,0.020194368,-0.010931527,0.055424098,0.0280142,-0.043165952,-0.019548746,0.0044855196,0.010485162,-0.02336834,-0.010721036,-0.01227679,-0.03940282,0.0026218442,-0.0034314084,0.045536183,-0.004990624,0.008738217,-0.008495131,0.014149939,-0.014554615,-0.00025271098,-0.026223144,-0.013117264,-0.007985059,0.02940574,0.025714621,-0.0084403325,-0.009303785,-0.0021680512,0.028929055,-0.018838583,0.0020801583,0.058907,-0.015284374,-0.0046584145,-0.015124302,0.01802365,-0.010142108,0.061019797,-0.06072787,0.009423724,0.022592576,0.036021184,0.042690862,-0.019374613,-0.036340505,0.0017474353,0.050691515,0.05715064,0.014848293,-0.020950966,0.01709487,-0.026171679,-0.015607574,-0.0243861,0.035336122,-0.015170756,-0.0008374521,0.007378496,-0.021755433,0.03209438,-0.008162412,-0.034740243,0.02452887,-0.0032301596,0.02880452,0.0095996,0.026667625,-0.009588203,0.037311267,-0.051684167,0.015775733,-0.015412584,0.032532085,0.06193363,0.011769409,0.0031633137,0.029037502,0.016771931,-0.007557208,-0.0077405847,0.015987564,-0.07137799,0.030197969,0.013752909,0.016586887,0.054434363,0.02789082,0.038726456,0.0489568,-0.030194554,0.00053031836,0.013833902,0.035553887,-0.033078812,0.032485943,-0.019619666,0.0064472486,0.018419694,-0.00034720977,0.036375456,-0.0044633397,-0.032902442,0.058506534,0.035415996,-0.014626198,-0.019019706,-0.03008869,0.0015747659,0.05839068,-0.028924098,-0.0012032049,0.026326768,-0.037551366,0.0017808278,0.003675074,-0.009056286,-0.015575959,-0.0557131,0.022994718,0.00040089796,0.045087438,0.012175033,-0.009842991,0.012461891,-0.011952326,-0.018429935,0.008043143,-0.027411968,0.018898198,0.0024562634,-0.017528757,-0.025718966,0.0015500226,-0.019514697,-0.024874642,0.030963806,-0.0043499917,0.06648538,0.051239308,0.028137157,-0.049439427,0.07121303,-0.02448218,0.0004288599,-0.005680335,-0.0066194106,-0.046563353,0.006058955,0.02344616,0.018996276,0.0046675717,-0.029190982,-0.0127839055,-0.051749468,-0.064143285,0.017800134,0.013017714,0.023475446,-0.019091338,-0.0018877747,-0.0264588,-0.017843187,-0.0010967759,-0.015491505,0.001821091,-0.024200981,-0.00029983043,-0.021971708,0.026671426,0.022811921,0.019768583,-0.0479457,0.008253267,0.042921018,-0.02842162,-0.010890284,-0.0064374106,-0.05421595,0.0258998,0.05558636,0.021569582,0.0073486106,-0.037526734,0.0013253619,-0.023693262,-0.03503779,0.010681625,-0.039948884,0.03399488,-0.061695233,0.0003446669,-0.045369208,0.0038619365,-0.03215089,-0.04403237,0.047550973,-0.009826077,0.015166153,0.017472787,0.002796071,-0.006034493,-0.010811644,0.059540596,-0.06078295,0.0036890542,0.018178543,-0.029992439,-0.19850528,-0.03194257,0.03377691,0.036154516,0.00490208,-0.0008334296,0.011646277,0.021954,-0.026440889,-0.050253615,-0.033171766,-0.13934325,0.009984324,-0.033362597,0.042912066,0.03196475,0.034773815,0.0076081525,-0.025372475,-0.07430217,0.020952784,0.0057277167,0.04322839,0.042983107,0.026063561,-0.0016730364,0.015275411,0.04221994,-0.022655874,0.014284312,-0.0463083,0.036476858,-0.03288044,-0.040318694,0.030074408,0.011591933,0.0020387606,-0.029649958,-0.0008538639,0.008965217,0.03825699,0.041144975,0.012965786,-0.057060402,0.023657579,-0.010350477,0.0019741298,-0.004375619,-0.0027016946,0.0406567,0.024622248,0.022610437,0.024564112,0.010864662,0.0038691903,0.027239624,0.0030704231,0.06092556,0.01384689,0.015776275,-0.0103877885,-0.053618427,-0.039439395,-0.03287674,0.0027006385,-0.011553846,-0.023262553,-0.0062862714,-0.013291545,-0.042805426,0.022500124,-0.004793406,-0.03972995,-0.04488412,-0.00089285563,-0.010487397,-0.027561193,-0.016426958,-0.008928625,-0.07306831,0.020609071,-0.004014864,-0.025722379,0.020290462,0.004794712,0.01182666,-0.027030837,-0.048572924,0.015904905,0.35731795,-0.044133615,-0.02994025,0.025063984,0.0063894377,0.007032747,-0.021961007,0.01964748,0.006305996,0.0038665633,-0.0025447952,0.0072978446,-0.0072591356,0.03679468,0.050066832,0.032922782,-0.026780559,0.003297853,-0.015320387,-0.020469684,0.007590191,-0.05691862,0.007009436,-0.0151808765,-0.03286695,-0.0015007539,0.0058216657,-0.013464914,-0.036334604,0.019893836,-0.029204644,-0.0025202746,-0.012483569,-0.051519208,-0.023118025,-0.012312459,-0.060566887,-0.028945757,-0.018603405,-0.022676706,-0.0016931158,-0.02908353,-0.0128353955,-0.010205085,-0.04297234,-0.018401492,0.0072227144,0.011113855,0.0072743464,-0.023970008,0.018455444,-0.03248694,-0.02390326,0.013589408,-0.012309607,-0.0014198754,-0.021615675,0.012658399,-0.01459343,-0.007875628,0.02061626,0.01622782,-0.030741706,0.03320389,0.046375185,0.011089998,-0.042191505,-0.017157875,0.010485263,0.033701383,0.05325507,0.008132996,0.048396595,0.0032107064,-0.015012059,-0.020909628,-0.016111596,0.018242432,-0.0030219932,0.02113477,-0.033761833,0.010786554,0.030459758,0.051399197,0.0070566996,0.012986552,-0.043599527,0.015188971,-0.0029194353,-0.033420116,0.0033106403,-0.014416731,-0.012618238,-0.00094905833,0.03960893,-0.008025231,0.010905686,-0.052109484,0.014604219,-0.023697235,0.03020492,0.026682433,0.0149353575,0.004931162,-0.009854375,-0.0064448616,0.0128806,-0.010819694,-0.02105376,0.006096209,-0.052785646,0.038573243,-1.7263766e-08,-0.0007205709,0.061116807,0.0044191824,0.027393492,0.020520587,0.0027436947,0.030333173,-0.046607006,-0.040806897,0.02305051,-7.9802216e-05,-0.01690014,-0.053705864,0.019314,-0.0056523914,0.00074979383,0.0013683812,0.020069424,0.018716196,-0.0045492644,-0.0025104564,-0.031064311,-0.020633329,0.03178816,9.4323615e-05,0.014527989,0.033540443,0.009221602,-0.05137457,0.008165044,0.023422886,-0.014989242,0.00093664625,0.014771565,-0.008061689,0.0027401303,-0.00026233195,0.016367186,-0.0027091913,0.037587896,-0.022341702,-0.004015708,-0.011983244,0.0034916876,-0.019291835,-0.03674534,-0.025383733,-0.0075318655,0.031157322,-0.010792413,0.06970341,-0.012267843,0.021670533,0.015414724,0.063724674,-0.024857124,0.008946904,0.014331233,-0.05553543,0.008260496,0.01497239,0.020899983,-0.04640809,-0.033817414,0.013087889,0.01997579,-0.009445559,0.028704895,0.016280014,-0.009083533,0.018562043,-0.034763645,-0.015725037,0.010356256,0.0100483345,0.016985439,-0.02429421,0.029589962,0.062435973,0.0143599985,-0.0057475287,-0.00041800225,0.012893603,-0.010494548,0.02445579,-0.010443274,0.016612478,0.02904424,0.027545024,0.00362994,0.029067067,0.0330074,-0.007848615,0.01218396,0.022781314,-0.011776866,-0.030050615,-0.01591636,0.0047324956,-0.018243222,-0.025002316,-0.004533823,-0.023591792,0.027656656,0.030145401,0.018555224,0.005789607,-0.030506507,-0.026731495,0.004842788,-0.048169944,-0.016486937,0.023139607,-0.00050537236,-0.043340217,-0.056943398,0.0019208333,-0.020342482,-0.03025713,0.0005417864,-0.041022044,-0.018942405,-0.008002942,0.004666964,0.0089818975,0.015391017,0.03132458,-0.008488269,-0.004263093,0.01825378,0.04567589,0.024361327,0.004392093,-0.039912447,0.029388713,-0.030569024,-0.008814221,-0.00819826,-0.022473043,-0.09476294,-0.034691468,0.03077441,0.074661955,0.005641624,-0.021404844,-0.009995672,0.0113968635,0.024320649,0.0065222294,0.005615123,-0.0058485973,0.009607924,0.017922776,-0.0315696,-0.05418342,-0.0014429255,-0.018145014,-0.01583405,-0.0426487,-0.01552698,-0.019694878,0.005286313,-0.031178964,0.010343735,-0.037648376,0.021937698,0.0022831636,-0.08227004,-0.0067213234,0.013539964,0.024329403,-0.0021142291,0.008727592,0.0015011195,0.0012257858,-0.010227084,0.020769406,0.02128747,0.0049409755,-0.04147786,-0.0352365,0.014319861,0.040656548,-0.051532663,0.04529006,0.03602142,0.017821115,-0.010344143,0.020150078,-0.004572456,-0.027475052,-0.004935057,0.024307659,0.017616566,0.019841185,0.006224667,0.01238387,0.009570616,-0.031509437,-0.031518552,-0.0010615875,-0.038004808,-0.02226355,-0.017153013,0.01153007,-0.018384183,-0.03475755,0.012761822,0.0012771795,-0.018212628,-0.00078549224,-0.027188381,-0.040488537,-0.026671525,-0.019761423,-0.019459859,-0.024763983,-0.017624186,0.024720237,0.02588202,0.0023750304,-0.017595857,-0.014765362,0.051325712,0.0019774942,0.008965136,-0.00021611899,-0.014104075,0.029231532,0.02047947,0.014482474,0.01962144,0.052980423,0.018516837,-0.024505593,-0.030480258,-0.01926896,0.027877364,0.0050631664,-0.023779634,0.037331447,0.013710907,-0.026467491,-0.011703273,0.026177274,0.012899756,-0.0010600702,-0.0014658107,0.04974223,0.055155005,0.02515965,-0.004355088,-0.01817613,0.003527142,-0.007938532,-0.010789809,-0.01018859,0.019622236,0.027463436,-0.032702602,-0.03845982,-0.053058956,0.024843846,0.027888672,0.012282223,0.004092791,-0.043852955,-0.0054676128,-0.0016720848,0.014899325,-0.039680637,0.015102061,0.02201449,-0.022749312,-0.028366677,-0.029756783,0.030943051,0.0021088633,0.008074596,0.021363307,0.004134791,-0.1145702,0.03238001,-0.037252095,-0.027966121,0.019064667,-0.0059560533,-0.004472691,-0.039005134,-0.018726662,-0.04787794,0.030923046,-0.018188838,0.0033480278,-0.04221418,-0.0141472565,0.002514261,-0.034130275,0.033664986,0.020599749,0.03823435,-0.0063169403,-0.04699824,-0.0466818,-0.035566125,0.009560613,0.050629444,-0.02088717,0.0174645,0.009180878,0.027218219,0.005903096,-0.013345459,-0.024641398,0.071969435,-0.024188334,0.02168025,-0.024974518,-0.047259077,0.011964847,0.034597978,0.038167298,-0.001629545,-0.04509996,0.0016243352,-0.026515108,-0.027910907,-0.00059185235,-0.010368403,0.0009602437,-0.0061133257,-0.0793666,0.00091349584,0.038366813,0.016520048,0.031976648,0.027472755,-0.005879314,-0.018116668,0.018921504,0.046142377,-0.004217135,0.032862626,-0.034764763,-0.022140559,0.0003004714,-0.007139821,-0.0062353374,-0.008036562,0.004582783,-0.010048349,-0.027682701,-0.05353367,0.0010917536,-0.017698983,0.047328144,-0.033986736,0.05022917,-0.0024929815,-0.008439714,-0.015441708,-0.011288038,-0.023091221,-0.024113398,0.059393074,0.05153617,-0.013979614,0.003861492,0.0005550814,-0.011603648,-0.016764436,-0.034307763,0.038863543,0.013517788,0.047070477,-0.035149295,0.01737924,0.0069022323,0.025809817,0.012658473,-0.004047336,0.039545044,0.05623425,-0.020864874,0.01477644,0.030067116,0.00833422,-0.027548488,0.0096238265,-0.024783188,0.00015246953,0.014378699,-0.060232773,-0.039958786,0.027679749,-0.0473303,2.8290335e-05,0.011902881,0.007599134,0.005901651,-0.02860964,0.034456026,0.04070535,-0.010780397,-0.053180918,0.02795018,0.005763763,-0.01865574,0.012152719,0.049603283,-0.0077415365,0.046241883,-0.02100746,-0.013696394,0.011409284,0.004809419,-0.015703633,-0.031949393,0.09516819,0.003963491,-0.01871322,-0.045259334,0.038522035,-0.0001968376,-0.027985236,-0.03523525,-0.026783247,0.004797099,-0.012033252,0.008109464,0.0066951294,-0.02040536,0.0057081566,0.026190674,0.006665419,-0.027425578,0.007462348,-0.017482188,-0.0453912,-0.021071622,0.036772586,0.034662023,-0.0070554805,-0.021229815,-0.038041756,-0.023566026,0.0002720465,-0.027888278,-0.042951964,0.0395614,0.016969787,-3.954311e-05,0.031676985,0.0016975867,0.041952327,0.047533363,-0.03106204,0.002312611,0.020308586,0.05630468,-0.0006247659,0.006417623,-0.03966842,0.062766515,-0.012217048,0.032767385,-0.019390363,-0.0072798287,0.0014770158,-0.018888947,0.033455096,-0.031959474,0.0427705,0.015732462,0.012656111,-0.009941254,-0.022283839,0.019452507,0.0026432828,-0.014799691,0.0019071322,-0.06523117,-0.024222316,-0.023673741,-0.008632875,-0.007842821,0.011944864,-0.03875177,-0.013826001,-0.014759893,-0.0030245485,-0.00018355147,0.027593207,0.017420406,0.055109274,0.014609997,0.021972025,-0.00665318,0.01680226,0.048829354,0.017262658,0.015886657,0.025455222,-0.03041837,-0.030935366,0.018494902,-0.042966157,0.0072807684,0.030723307,-0.041682966,-0.0015760143,-0.011461887,0.036754563,0.027119072,0.031386487,0.0156200435,0.016561568,0.004275566,0.046683095,-0.006489172,0.012556084,-0.025990639,-0.02961862,0.0026811864,0.01594583,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]', 0, '# Python编程从入门到实践：核心概念与实战指南

## 目录
1. Python语言概述
2. 开发环境搭建
3. 基础语法详解
4. 面向对象编程
5. 异常处理机制
6. 文件操作与持久化
7. 实战项目：Web应用开发
8. 调试与测试
9. 性能优化技巧
10. 部署与维护

---

## 1. Python语言概述
### 1.1 Python的特点
- 解释型语言，无需编译
- 动态类型系统
- 丰富的标准库和第三方库
- 跨平台运行能力

### 1.2 应用领域
- Web开发（Django/Flask）
- 数据科学（Pandas/Numpy）
- 人工智能（TensorFlow/PyTorch）
- 自动化脚本
- 网络爬虫

---

## 2. 开发环境搭建
### 2.1 安装Python
```bash
# Ubuntu系统
sudo apt update
sudo apt install python3

# macOS系统
brew install python

# Windows系统
下载安装包：https://www.python.org/downloads/
```

### 2.2 虚拟环境配置
```python
# 创建虚拟环境
python3 -m venv myenv

# 激活环境
# Linux/macOS
source myenv/bin/activate
# Windows
myenv\Scripts\activate
```

### 2.3 IDE选择建议
- PyCharm（专业版/社区版）
- VS Code（安装Python插件）
- Jupyter Notebook（数据科学首选）

---

## 3. 基础语法详解
### 3.1 变量与数据类型
```python
# 基本类型示例
age = 25                # 整型
price = 19.99           # 浮点型
name = "Alice"          # 字符串
is_student = True       # 布尔型
complex_num = 3 + 4j    # 复数

# 容器类型
fruits = ["apple", "banana", "cherry"]  # 列表
person = {"name": "Bob", "age": 30}     # 字典
unique_numbers = {1, 2, 3, 3}           # 集合
```

### 3.2 控制结构
```python
# 条件语句
score = 85
if score >= 90:
    print("A")
elif score >= 80:
    print("B")
else:
    print("C")

# 循环结构
# for循环
for i in range(5):
    print(i)

# while循环
count = 0
while count < 5:
    print(count)
    count += 1
```

### 3.3 函数定义
```python
def calculate_area(radius):
    """计算圆面积"""
    return 3.14159 * radius ** 2

# 使用lambda表达式
square = lambda x: x * x
print(square(5))  # 输出25
```

### 3.4 列表推导式
```python
# 生成平方数列表
squares = [x**2 for x in range(10)]
# 带条件过滤
even_squares = [x**2 for x in range(10) if x % 2 == 0]
```

---

## 4. 面向对象编程
### 4.1 类定义
```python
class Vehicle:
    def __init__(self, brand, model):
        self.brand = brand
        self.model = model

    def display_info(self):
        return f"{self.brand} {self.model}"

class Car(Vehicle):
    def __init__(self, brand, model, num_doors):
        super().__init__(brand, model)
        self.num_doors = num_doors

    def display_info(self):
        return f"{super().display_info()} with {self.num_doors} doors"
```

### 4.2 魔术方法
```python
class Point:
    def __init__(self, x, y):
        self.x = x
        self.y = y

    def __str__(self):
        return f"Point({self.x}, {self.y})"

    def __add__(self, other):
        return Point(self.x + other.x, self.y + other.y)
```

### 4.3 类方法与静态方法
```python
class MathUtils:
    @staticmethod
    def add(a, b):
        return a + b

    @classmethod
    def from_string(cls, string):
        x, y = map(int, string.split('',''))
        return cls(x, y)
```

---

## 5. 异常处理机制
```python
try:
    result = 10 / 0
except ZeroDivisionError as e:
    print(f"Error: {e}")
finally:
    print("Execution completed")

# 自定义异常
class CustomError(Exception):
    def __init__(self, message):
        self.message = message
```

---

## 6. 文件操作与持久化
### 6.1 基本文件操作
```python
# 写入文件
with open(''data.txt'', ''w'') as f:
    f.write("Hello World")

# 读取文件
with open(''data.txt'', ''r'') as f:
    content = f.read()
```

### 6.2 使用Pickle序列化
```python
import pickle

data = {''name'': ''Alice'', ''age'': 30}
with open(''data.pkl'', ''wb'') as f:
    pickle.dump(data, f)

with open(''data.pkl'', ''rb'') as f:
    loaded_data = pickle.load(f)
```

### 6.3 CSV文件处理
```python
import csv

# 写入CSV
with open(''data.csv'', ''w'', newline='''') as f:
    writer = csv.writer(f)
    writer.writerow([''Name'', ''Age''])
    writer.writerow([''Alice'', 30])

# 读取CSV
with open(''data.csv'', ''r'') as f:
    reader = csv.DictReader(f)
    for row in reader:
        print(row[''Name''], row[''Age''])
```

---

## 7. 实战项目：Web应用开发
### 7.1 使用Flask框架
```python
from flask import Flask, render_template, request

app = Flask(__name__)

@app.route(''/'')
def index():
    return "Hello, World!"

@app.route(''/greet/<name>'')
def greet(name):
    return f"Hello, {name}!"

if __name__ == ''__main__'':
    app.run(debug=True)
```

### 7.2 数据库集成
```python
from flask_sqlalchemy import SQLAlchemy

app.config[''SQLALCHEMY_DATABASE_URI''] = ''sqlite:///site.db''
db = SQLAlchemy(app)

class User(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    username = db.Column(db.String(20), unique=True, nullable=False)
    email = db.Column(db.String(120), unique=True, nullable=False)

    def __repr__(self):
        return f"User(''{self.username}'', ''{self.email}'')"
```

---

## 8. 调试与测试
### 8.1 使用pdb调试
```python
import pdb

def calculate_sum(a, b):
    pdb.set_trace()
    return a + b

calculate_sum(3, 5)
```

### 8.2 单元测试
```python
import unittest

class TestMathOperations(unittest.TestCase):
    def test_add(self):
        self.assertEqual(1 + 1, 2)

    def test_subtract(self):
        self.assertEqual(3 - 1, 2)

if __name__ == ''__main__'':
    unittest.main()
```

---

## 9. 性能优化技巧
### 9.1 算法优化
```python
# 低效实现
def sum_n(n):
    total = 0
    for i in range(n+1):
        total += i
    return total

# 高效实现
def sum_n(n):
    return n * (n + 1) // 2
```

### 9.2 内存管理
```python
# 使用生成器节省内存
def read_large_file(file_path):
    with open(file_path, ''r'') as f:
        for line in f:
            yield line
```

### 9.3 并行计算
```python
from multiprocessing import Pool

def process_data(data):
    # 数据处理逻辑
    return processed_data

if __name__ == ''__main__'':
    with Pool(4) as p:
        results = p.map(process_data, large_dataset)
```

---

## 10. 部署与维护
### 10.1 使用Docker容器化
```dockerfile
# Dockerfile示例
FROM python:3.9-slim
WORKDIR /app
COPY requirements.txt .
RUN pip install -r requirements.txt
COPY . .
CMD ["python", "app.py"]
```

### 10.2 持续集成配置
```yaml
# GitHub Actions示例
name: Python CI

on: [push]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v2
    - name: Set up Python
      uses: actions/setup-python@v2
      with:
        python-version: 3.9
    - name: Install dependencies
      run: |
        python -m pip install --upgrade pip
        pip install -r requirements.txt
    - name: Test
      run: pytest
```

---', NULL, '2025-04-04 17:18:26.391235');

-- ----------------------------
-- Table structure for kb_document_version
-- ----------------------------
DROP TABLE IF EXISTS "public"."kb_document_version";
CREATE TABLE "public"."kb_document_version" (
  "id" int8 NOT NULL DEFAULT nextval('kb_document_version_id_seq'::regclass),
  "document_id" int8 NOT NULL,
  "version" int4 NOT NULL,
  "title" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
  "content" text COLLATE "pg_catalog"."default",
  "file_url" varchar(500) COLLATE "pg_catalog"."default",
  "creator_id" int8 NOT NULL,
  "create_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
  "deleted" int4 DEFAULT 0
)
;

-- ----------------------------
-- Records of kb_document_version
-- ----------------------------
INSERT INTO "public"."kb_document_version" VALUES (42, 42, 1, 'Python编程从入门到实践：核心概念与实战指南.md', '# Python编程从入门到实践：核心概念与实战指南

## 目录
1. Python语言概述
2. 开发环境搭建
3. 基础语法详解
4. 面向对象编程
5. 异常处理机制
6. 文件操作与持久化
7. 实战项目：Web应用开发
8. 调试与测试
9. 性能优化技巧
10. 部署与维护

---

## 1. Python语言概述
### 1.1 Python的特点
- 解释型语言，无需编译
- 动态类型系统
- 丰富的标准库和第三方库
- 跨平台运行能力

### 1.2 应用领域
- Web开发（Django/Flask）
- 数据科学（Pandas/Numpy）
- 人工智能（TensorFlow/PyTorch）
- 自动化脚本
- 网络爬虫

---

## 2. 开发环境搭建
### 2.1 安装Python
```bash
# Ubuntu系统
sudo apt update
sudo apt install python3

# macOS系统
brew install python

# Windows系统
下载安装包：https://www.python.org/downloads/
```

### 2.2 虚拟环境配置
```python
# 创建虚拟环境
python3 -m venv myenv

# 激活环境
# Linux/macOS
source myenv/bin/activate
# Windows
myenv\Scripts\activate
```

### 2.3 IDE选择建议
- PyCharm（专业版/社区版）
- VS Code（安装Python插件）
- Jupyter Notebook（数据科学首选）

---

## 3. 基础语法详解
### 3.1 变量与数据类型
```python
# 基本类型示例
age = 25                # 整型
price = 19.99           # 浮点型
name = "Alice"          # 字符串
is_student = True       # 布尔型
complex_num = 3 + 4j    # 复数

# 容器类型
fruits = ["apple", "banana", "cherry"]  # 列表
person = {"name": "Bob", "age": 30}     # 字典
unique_numbers = {1, 2, 3, 3}           # 集合
```

### 3.2 控制结构
```python
# 条件语句
score = 85
if score >= 90:
    print("A")
elif score >= 80:
    print("B")
else:
    print("C")

# 循环结构
# for循环
for i in range(5):
    print(i)

# while循环
count = 0
while count < 5:
    print(count)
    count += 1
```

### 3.3 函数定义
```python
def calculate_area(radius):
    """计算圆面积"""
    return 3.14159 * radius ** 2

# 使用lambda表达式
square = lambda x: x * x
print(square(5))  # 输出25
```

### 3.4 列表推导式
```python
# 生成平方数列表
squares = [x**2 for x in range(10)]
# 带条件过滤
even_squares = [x**2 for x in range(10) if x % 2 == 0]
```

---

## 4. 面向对象编程
### 4.1 类定义
```python
class Vehicle:
    def __init__(self, brand, model):
        self.brand = brand
        self.model = model

    def display_info(self):
        return f"{self.brand} {self.model}"

class Car(Vehicle):
    def __init__(self, brand, model, num_doors):
        super().__init__(brand, model)
        self.num_doors = num_doors

    def display_info(self):
        return f"{super().display_info()} with {self.num_doors} doors"
```

### 4.2 魔术方法
```python
class Point:
    def __init__(self, x, y):
        self.x = x
        self.y = y

    def __str__(self):
        return f"Point({self.x}, {self.y})"

    def __add__(self, other):
        return Point(self.x + other.x, self.y + other.y)
```

### 4.3 类方法与静态方法
```python
class MathUtils:
    @staticmethod
    def add(a, b):
        return a + b

    @classmethod
    def from_string(cls, string):
        x, y = map(int, string.split('',''))
        return cls(x, y)
```

---

## 5. 异常处理机制
```python
try:
    result = 10 / 0
except ZeroDivisionError as e:
    print(f"Error: {e}")
finally:
    print("Execution completed")

# 自定义异常
class CustomError(Exception):
    def __init__(self, message):
        self.message = message
```

---

## 6. 文件操作与持久化
### 6.1 基本文件操作
```python
# 写入文件
with open(''data.txt'', ''w'') as f:
    f.write("Hello World")

# 读取文件
with open(''data.txt'', ''r'') as f:
    content = f.read()
```

### 6.2 使用Pickle序列化
```python
import pickle

data = {''name'': ''Alice'', ''age'': 30}
with open(''data.pkl'', ''wb'') as f:
    pickle.dump(data, f)

with open(''data.pkl'', ''rb'') as f:
    loaded_data = pickle.load(f)
```

### 6.3 CSV文件处理
```python
import csv

# 写入CSV
with open(''data.csv'', ''w'', newline='''') as f:
    writer = csv.writer(f)
    writer.writerow([''Name'', ''Age''])
    writer.writerow([''Alice'', 30])

# 读取CSV
with open(''data.csv'', ''r'') as f:
    reader = csv.DictReader(f)
    for row in reader:
        print(row[''Name''], row[''Age''])
```

---

## 7. 实战项目：Web应用开发
### 7.1 使用Flask框架
```python
from flask import Flask, render_template, request

app = Flask(__name__)

@app.route(''/'')
def index():
    return "Hello, World!"

@app.route(''/greet/<name>'')
def greet(name):
    return f"Hello, {name}!"

if __name__ == ''__main__'':
    app.run(debug=True)
```

### 7.2 数据库集成
```python
from flask_sqlalchemy import SQLAlchemy

app.config[''SQLALCHEMY_DATABASE_URI''] = ''sqlite:///site.db''
db = SQLAlchemy(app)

class User(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    username = db.Column(db.String(20), unique=True, nullable=False)
    email = db.Column(db.String(120), unique=True, nullable=False)

    def __repr__(self):
        return f"User(''{self.username}'', ''{self.email}'')"
```

---

## 8. 调试与测试
### 8.1 使用pdb调试
```python
import pdb

def calculate_sum(a, b):
    pdb.set_trace()
    return a + b

calculate_sum(3, 5)
```

### 8.2 单元测试
```python
import unittest

class TestMathOperations(unittest.TestCase):
    def test_add(self):
        self.assertEqual(1 + 1, 2)

    def test_subtract(self):
        self.assertEqual(3 - 1, 2)

if __name__ == ''__main__'':
    unittest.main()
```

---

## 9. 性能优化技巧
### 9.1 算法优化
```python
# 低效实现
def sum_n(n):
    total = 0
    for i in range(n+1):
        total += i
    return total

# 高效实现
def sum_n(n):
    return n * (n + 1) // 2
```

### 9.2 内存管理
```python
# 使用生成器节省内存
def read_large_file(file_path):
    with open(file_path, ''r'') as f:
        for line in f:
            yield line
```

### 9.3 并行计算
```python
from multiprocessing import Pool

def process_data(data):
    # 数据处理逻辑
    return processed_data

if __name__ == ''__main__'':
    with Pool(4) as p:
        results = p.map(process_data, large_dataset)
```

---

## 10. 部署与维护
### 10.1 使用Docker容器化
```dockerfile
# Dockerfile示例
FROM python:3.9-slim
WORKDIR /app
COPY requirements.txt .
RUN pip install -r requirements.txt
COPY . .
CMD ["python", "app.py"]
```

### 10.2 持续集成配置
```yaml
# GitHub Actions示例
name: Python CI

on: [push]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v2
    - name: Set up Python
      uses: actions/setup-python@v2
      with:
        python-version: 3.9
    - name: Install dependencies
      run: |
        python -m pip install --upgrade pip
        pip install -r requirements.txt
    - name: Test
      run: pytest
```

---

', '/snow-ai/d2e70c8564d5472b885f3bb494739389.md', 1, '2025-04-04 17:01:18.700376', 0);
INSERT INTO "public"."kb_document_version" VALUES (44, 44, 1, 'Python编程从入门到实践：核心概念与实战指南.md', '# Python编程从入门到实践：核心概念与实战指南

## 目录
1. Python语言概述
2. 开发环境搭建
3. 基础语法详解
4. 面向对象编程
5. 异常处理机制
6. 文件操作与持久化
7. 实战项目：Web应用开发
8. 调试与测试
9. 性能优化技巧
10. 部署与维护

---

## 1. Python语言概述
### 1.1 Python的特点
- 解释型语言，无需编译
- 动态类型系统
- 丰富的标准库和第三方库
- 跨平台运行能力

### 1.2 应用领域
- Web开发（Django/Flask）
- 数据科学（Pandas/Numpy）
- 人工智能（TensorFlow/PyTorch）
- 自动化脚本
- 网络爬虫

---

## 2. 开发环境搭建
### 2.1 安装Python
```bash
# Ubuntu系统
sudo apt update
sudo apt install python3

# macOS系统
brew install python

# Windows系统
下载安装包：https://www.python.org/downloads/
```

### 2.2 虚拟环境配置
```python
# 创建虚拟环境
python3 -m venv myenv

# 激活环境
# Linux/macOS
source myenv/bin/activate
# Windows
myenv\Scripts\activate
```

### 2.3 IDE选择建议
- PyCharm（专业版/社区版）
- VS Code（安装Python插件）
- Jupyter Notebook（数据科学首选）

---

## 3. 基础语法详解
### 3.1 变量与数据类型
```python
# 基本类型示例
age = 25                # 整型
price = 19.99           # 浮点型
name = "Alice"          # 字符串
is_student = True       # 布尔型
complex_num = 3 + 4j    # 复数

# 容器类型
fruits = ["apple", "banana", "cherry"]  # 列表
person = {"name": "Bob", "age": 30}     # 字典
unique_numbers = {1, 2, 3, 3}           # 集合
```

### 3.2 控制结构
```python
# 条件语句
score = 85
if score >= 90:
    print("A")
elif score >= 80:
    print("B")
else:
    print("C")

# 循环结构
# for循环
for i in range(5):
    print(i)

# while循环
count = 0
while count < 5:
    print(count)
    count += 1
```

### 3.3 函数定义
```python
def calculate_area(radius):
    """计算圆面积"""
    return 3.14159 * radius ** 2

# 使用lambda表达式
square = lambda x: x * x
print(square(5))  # 输出25
```

### 3.4 列表推导式
```python
# 生成平方数列表
squares = [x**2 for x in range(10)]
# 带条件过滤
even_squares = [x**2 for x in range(10) if x % 2 == 0]
```

---

## 4. 面向对象编程
### 4.1 类定义
```python
class Vehicle:
    def __init__(self, brand, model):
        self.brand = brand
        self.model = model

    def display_info(self):
        return f"{self.brand} {self.model}"

class Car(Vehicle):
    def __init__(self, brand, model, num_doors):
        super().__init__(brand, model)
        self.num_doors = num_doors

    def display_info(self):
        return f"{super().display_info()} with {self.num_doors} doors"
```

### 4.2 魔术方法
```python
class Point:
    def __init__(self, x, y):
        self.x = x
        self.y = y

    def __str__(self):
        return f"Point({self.x}, {self.y})"

    def __add__(self, other):
        return Point(self.x + other.x, self.y + other.y)
```

### 4.3 类方法与静态方法
```python
class MathUtils:
    @staticmethod
    def add(a, b):
        return a + b

    @classmethod
    def from_string(cls, string):
        x, y = map(int, string.split('',''))
        return cls(x, y)
```

---

## 5. 异常处理机制
```python
try:
    result = 10 / 0
except ZeroDivisionError as e:
    print(f"Error: {e}")
finally:
    print("Execution completed")

# 自定义异常
class CustomError(Exception):
    def __init__(self, message):
        self.message = message
```

---

## 6. 文件操作与持久化
### 6.1 基本文件操作
```python
# 写入文件
with open(''data.txt'', ''w'') as f:
    f.write("Hello World")

# 读取文件
with open(''data.txt'', ''r'') as f:
    content = f.read()
```

### 6.2 使用Pickle序列化
```python
import pickle

data = {''name'': ''Alice'', ''age'': 30}
with open(''data.pkl'', ''wb'') as f:
    pickle.dump(data, f)

with open(''data.pkl'', ''rb'') as f:
    loaded_data = pickle.load(f)
```

### 6.3 CSV文件处理
```python
import csv

# 写入CSV
with open(''data.csv'', ''w'', newline='''') as f:
    writer = csv.writer(f)
    writer.writerow([''Name'', ''Age''])
    writer.writerow([''Alice'', 30])

# 读取CSV
with open(''data.csv'', ''r'') as f:
    reader = csv.DictReader(f)
    for row in reader:
        print(row[''Name''], row[''Age''])
```

---

## 7. 实战项目：Web应用开发
### 7.1 使用Flask框架
```python
from flask import Flask, render_template, request

app = Flask(__name__)

@app.route(''/'')
def index():
    return "Hello, World!"

@app.route(''/greet/<name>'')
def greet(name):
    return f"Hello, {name}!"

if __name__ == ''__main__'':
    app.run(debug=True)
```

### 7.2 数据库集成
```python
from flask_sqlalchemy import SQLAlchemy

app.config[''SQLALCHEMY_DATABASE_URI''] = ''sqlite:///site.db''
db = SQLAlchemy(app)

class User(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    username = db.Column(db.String(20), unique=True, nullable=False)
    email = db.Column(db.String(120), unique=True, nullable=False)

    def __repr__(self):
        return f"User(''{self.username}'', ''{self.email}'')"
```

---

## 8. 调试与测试
### 8.1 使用pdb调试
```python
import pdb

def calculate_sum(a, b):
    pdb.set_trace()
    return a + b

calculate_sum(3, 5)
```

### 8.2 单元测试
```python
import unittest

class TestMathOperations(unittest.TestCase):
    def test_add(self):
        self.assertEqual(1 + 1, 2)

    def test_subtract(self):
        self.assertEqual(3 - 1, 2)

if __name__ == ''__main__'':
    unittest.main()
```

---

## 9. 性能优化技巧
### 9.1 算法优化
```python
# 低效实现
def sum_n(n):
    total = 0
    for i in range(n+1):
        total += i
    return total

# 高效实现
def sum_n(n):
    return n * (n + 1) // 2
```

### 9.2 内存管理
```python
# 使用生成器节省内存
def read_large_file(file_path):
    with open(file_path, ''r'') as f:
        for line in f:
            yield line
```

### 9.3 并行计算
```python
from multiprocessing import Pool

def process_data(data):
    # 数据处理逻辑
    return processed_data

if __name__ == ''__main__'':
    with Pool(4) as p:
        results = p.map(process_data, large_dataset)
```

---

## 10. 部署与维护
### 10.1 使用Docker容器化
```dockerfile
# Dockerfile示例
FROM python:3.9-slim
WORKDIR /app
COPY requirements.txt .
RUN pip install -r requirements.txt
COPY . .
CMD ["python", "app.py"]
```

### 10.2 持续集成配置
```yaml
# GitHub Actions示例
name: Python CI

on: [push]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v2
    - name: Set up Python
      uses: actions/setup-python@v2
      with:
        python-version: 3.9
    - name: Install dependencies
      run: |
        python -m pip install --upgrade pip
        pip install -r requirements.txt
    - name: Test
      run: pytest
```

---

', '/snow-ai/5f6abb1a20bd44ac8d1d9834eda50d85.md', 1, '2025-04-04 17:11:14.278697', 0);

-- ----------------------------
-- Table structure for kb_knowledge_base
-- ----------------------------
DROP TABLE IF EXISTS "public"."kb_knowledge_base";
CREATE TABLE "public"."kb_knowledge_base" (
  "id" int8 NOT NULL DEFAULT nextval('kb_knowledge_base_id_seq'::regclass),
  "name" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "description" text COLLATE "pg_catalog"."default",
  "creator_id" int8 NOT NULL,
  "dept_id" int8,
  "status" int4 DEFAULT 1,
  "create_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
  "update_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
  "deleted" int4 DEFAULT 0,
  "category_id" int8
)
;

-- ----------------------------
-- Records of kb_knowledge_base
-- ----------------------------
INSERT INTO "public"."kb_knowledge_base" VALUES (32, 'Python知识库', '这是一个Python知识库', 1, NULL, 1, '2025-04-04 16:49:40.754014', '2025-04-04 16:49:40.754014', 0, 3);

-- ----------------------------
-- Table structure for kb_knowledge_base_permission
-- ----------------------------
DROP TABLE IF EXISTS "public"."kb_knowledge_base_permission";
CREATE TABLE "public"."kb_knowledge_base_permission" (
  "id" int8 NOT NULL DEFAULT nextval('kb_knowledge_base_permission_id_seq'::regclass),
  "kb_id" int8 NOT NULL,
  "user_id" int8,
  "role_id" int8,
  "permission_type" int4 NOT NULL,
  "create_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
  "update_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP
)
;

-- ----------------------------
-- Records of kb_knowledge_base_permission
-- ----------------------------
INSERT INTO "public"."kb_knowledge_base_permission" VALUES (32, 32, 1, NULL, 3, '2025-04-04 16:49:40.757014', '2025-04-04 16:49:40.757014');

-- ----------------------------
-- Table structure for kb_tag
-- ----------------------------
DROP TABLE IF EXISTS "public"."kb_tag";
CREATE TABLE "public"."kb_tag" (
  "id" int8 NOT NULL DEFAULT nextval('kb_tag_id_seq'::regclass),
  "name" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "description" text COLLATE "pg_catalog"."default",
  "kb_id" int8,
  "creator_id" int8 NOT NULL,
  "status" int4 DEFAULT 1,
  "create_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
  "update_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
  "deleted" int4 DEFAULT 0
)
;

-- ----------------------------
-- Records of kb_tag
-- ----------------------------
INSERT INTO "public"."kb_tag" VALUES (18, 'AI', NULL, NULL, 1, 1, '2025-04-04 16:48:25.437748', '2025-04-04 16:48:25.437748', 0);
INSERT INTO "public"."kb_tag" VALUES (19, '机器学习', NULL, NULL, 1, 1, '2025-04-04 16:48:30.596239', '2025-04-04 16:48:30.596239', 0);
INSERT INTO "public"."kb_tag" VALUES (20, '深度学习', NULL, NULL, 1, 1, '2025-04-04 16:48:34.907911', '2025-04-04 16:48:34.907911', 0);
INSERT INTO "public"."kb_tag" VALUES (21, '自然语言处理', NULL, NULL, 1, 1, '2025-04-04 16:48:39.130701', '2025-04-04 16:48:39.130701', 0);
INSERT INTO "public"."kb_tag" VALUES (22, '计算机视觉', NULL, NULL, 1, 1, '2025-04-04 16:48:43.736814', '2025-04-04 16:48:43.736814', 0);
INSERT INTO "public"."kb_tag" VALUES (23, 'Python', NULL, NULL, 1, 1, '2025-04-04 17:01:06.484201', '2025-04-04 17:01:06.484201', 0);

-- ----------------------------
-- Table structure for kb_user_behavior
-- ----------------------------
DROP TABLE IF EXISTS "public"."kb_user_behavior";
CREATE TABLE "public"."kb_user_behavior" (
  "id" int8 NOT NULL DEFAULT nextval('kb_user_behavior_id_seq'::regclass),
  "user_id" int8 NOT NULL,
  "kb_id" int8 NOT NULL,
  "doc_id" int8 NOT NULL,
  "behavior_type" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
  "create_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP
)
;

-- ----------------------------
-- Records of kb_user_behavior
-- ----------------------------

-- ----------------------------
-- Table structure for llm_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."llm_config";
CREATE TABLE "public"."llm_config" (
  "id" int8 NOT NULL DEFAULT nextval('llm_config_id_seq'::regclass),
  "model_name" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "api_url" varchar(500) COLLATE "pg_catalog"."default" NOT NULL,
  "api_key" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
  "enabled" bool DEFAULT true,
  "create_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
  "update_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
  "model_type" varchar(255) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "model_code" varchar(255) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "model_provider" varchar(255) COLLATE "pg_catalog"."default" DEFAULT ''::character varying
)
;

-- ----------------------------
-- Records of llm_config
-- ----------------------------
INSERT INTO "public"."llm_config" VALUES (1, 'GPT-3.5-turbo', 'https://api.openai.com/v1/chat/completions', 'your-api-key', 't', '2025-03-22 09:28:31.684262', '2025-03-22 09:28:31.684262', 'GENERAL', 'GPT-3.5-turbo', 'openai');
INSERT INTO "public"."llm_config" VALUES (7, 'DeepSeek R1', 'https://api.deepseek.com/chat/completions', 'your-api-key', 't', '2025-04-03 09:21:50.025736', '2025-04-03 09:21:50.025736', 'REASONING', 'deepseek-reasoner', 'deepseek');
INSERT INTO "public"."llm_config" VALUES (5, 'qwen-plus', 'https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions', 'your-api-key', 't', '2025-03-26 13:57:28.172103', '2025-03-26 13:57:28.172103', 'GENERAL', 'qwen-plus', 'alibaba');
INSERT INTO "public"."llm_config" VALUES (4, 'glm-zero-preview', 'https://open.bigmodel.cn/api/paas/v4/chat/completions', 'your-api-key', 't', '2025-03-26 13:35:41.86654', '2025-03-26 13:35:41.86654', 'GENERAL', 'glm-zero-preview', 'zhipu');
INSERT INTO "public"."llm_config" VALUES (2, 'glm-4-plus', 'https://open.bigmodel.cn/api/paas/v4/chat/completions', 'your-api-key', 't', '2025-03-23 21:20:00.46653', '2025-03-23 21:20:00.46653', 'GENERAL', 'glm-4-plus', 'zhipu');
INSERT INTO "public"."llm_config" VALUES (6, 'deepseek-r1:1.5b', 'http://localhost:11434/api/generate', 'your-api-key', 't', '2025-04-02 15:06:23.290548', '2025-04-02 15:06:23.290548', 'GENERAL', 'deepseek-r1:1.5b', 'ollama');

-- ----------------------------
-- Table structure for llm_token_usage
-- ----------------------------
DROP TABLE IF EXISTS "public"."llm_token_usage";
CREATE TABLE "public"."llm_token_usage" (
  "id" int8 NOT NULL DEFAULT nextval('llm_token_usage_id_seq'::regclass),
  "model_id" int8 NOT NULL,
  "tokens_used" int4 NOT NULL,
  "create_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
  "update_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP
)
;

-- ----------------------------
-- Records of llm_token_usage
-- ----------------------------

-- ----------------------------
-- Table structure for sys_ai_model
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_ai_model";
CREATE TABLE "public"."sys_ai_model" (
  "id" int8 NOT NULL DEFAULT nextval('sys_ai_model_id_seq'::regclass),
  "model_name" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "model_type" int4 NOT NULL,
  "api_url" varchar(500) COLLATE "pg_catalog"."default" NOT NULL,
  "api_key" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
  "status" int4 DEFAULT 1,
  "deleted" int4 DEFAULT 0,
  "create_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
  "update_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP
)
;

-- ----------------------------
-- Records of sys_ai_model
-- ----------------------------
INSERT INTO "public"."sys_ai_model" VALUES (1, 'GPT-3.5-turbo', 1, 'https://api.openai.com/v1/chat/completions', 'your-api-key', 1, 0, '2025-03-22 09:28:31.676908', '2025-03-22 09:28:31.676908');

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_dept";
CREATE TABLE "public"."sys_dept" (
  "id" int8 NOT NULL DEFAULT nextval('sys_dept_id_seq'::regclass),
  "parent_id" int8,
  "dept_name" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "sort" int4 DEFAULT 0,
  "leader" varchar(50) COLLATE "pg_catalog"."default",
  "phone" varchar(20) COLLATE "pg_catalog"."default",
  "email" varchar(100) COLLATE "pg_catalog"."default",
  "status" int4 DEFAULT 1,
  "deleted" int4 DEFAULT 0,
  "create_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
  "update_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP
)
;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO "public"."sys_dept" VALUES (1, 0, '总公司', 1, 'admin', '13800000000', 'admin@example.com', 1, 0, '2025-03-22 09:28:31.608872', '2025-03-22 09:28:31.608872');

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_permission";
CREATE TABLE "public"."sys_permission" (
  "id" int8 NOT NULL DEFAULT nextval('sys_permission_id_seq'::regclass),
  "parent_id" int8,
  "name" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "type" int4 NOT NULL,
  "permission_code" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "path" varchar(200) COLLATE "pg_catalog"."default",
  "component" varchar(200) COLLATE "pg_catalog"."default",
  "icon" varchar(100) COLLATE "pg_catalog"."default",
  "sort" int4 DEFAULT 0,
  "status" int4 DEFAULT 1,
  "deleted" int4 DEFAULT 0,
  "create_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
  "update_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP
)
;

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
INSERT INTO "public"."sys_permission" VALUES (1, 0, '系统管理', 1, 'system:manage', '/system', 'Layout', 'system', 1, 1, 0, '2025-03-22 09:28:31.634771', '2025-03-22 09:28:31.634771');
INSERT INTO "public"."sys_permission" VALUES (2, 1, '用户管理', 1, 'system:user', '/system/user', 'system/user/index', 'user', 1, 1, 0, '2025-03-22 09:28:31.634771', '2025-03-22 09:28:31.634771');
INSERT INTO "public"."sys_permission" VALUES (3, 1, '角色管理', 1, 'system:role', '/system/role', 'system/role/index', 'role', 2, 1, 0, '2025-03-22 09:28:31.634771', '2025-03-22 09:28:31.634771');
INSERT INTO "public"."sys_permission" VALUES (4, 1, '权限管理', 1, 'system:permission', '/system/permission', 'system/permission/index', 'permission', 3, 1, 0, '2025-03-22 09:28:31.634771', '2025-03-22 09:28:31.634771');
INSERT INTO "public"."sys_permission" VALUES (5, 1, '部门管理', 1, 'system:dept', '/system/dept', 'system/dept/index', 'dept', 4, 1, 0, '2025-03-22 09:28:31.634771', '2025-03-22 09:28:31.634771');
INSERT INTO "public"."sys_permission" VALUES (7, 6, '知识库列表', 1, 'kb:list', '/kb/list', 'kb/list/index', 'kb-list', 1, 1, 0, '2025-03-22 09:28:31.634771', '2025-03-22 09:28:31.634771');
INSERT INTO "public"."sys_permission" VALUES (8, 6, '文档管理', 1, 'kb:document', '/kb/document', 'kb/document/index', 'document', 2, 1, 0, '2025-03-22 09:28:31.634771', '2025-03-22 09:28:31.634771');
INSERT INTO "public"."sys_permission" VALUES (9, 6, '分类管理', 1, 'kb:category', '/kb/category', 'kb/category/index', 'category', 3, 1, 0, '2025-03-22 09:28:31.634771', '2025-03-22 09:28:31.634771');
INSERT INTO "public"."sys_permission" VALUES (10, 6, '标签管理', 1, 'kb:tag', '/kb/tag', 'kb/tag/index', 'tag', 4, 1, 0, '2025-03-22 09:28:31.634771', '2025-03-22 09:28:31.634771');
INSERT INTO "public"."sys_permission" VALUES (6, 0, '知识库管理', 1, 'kb:manage', '/kb', 'Layout', 'kb', 2, 1, 0, '2025-03-22 09:28:31.634771', '2025-03-22 09:28:31.634771');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_role";
CREATE TABLE "public"."sys_role" (
  "id" int8 NOT NULL DEFAULT nextval('sys_role_id_seq'::regclass),
  "role_name" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "role_code" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "description" text COLLATE "pg_catalog"."default",
  "status" int4 DEFAULT 1,
  "deleted" int4 DEFAULT 0,
  "create_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
  "update_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP
)
;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO "public"."sys_role" VALUES (2, '普通用户', 'ROLE_USER', '普通用户11', 1, 0, '2025-03-22 09:28:31.623349', '2025-03-22 09:28:31.623349');
INSERT INTO "public"."sys_role" VALUES (1, '超级管理员', 'ROLE_ADMIN', '系统超级管理员', 1, 0, '2025-03-22 09:28:31.623349', '2025-03-22 09:28:31.623349');

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_role_permission";
CREATE TABLE "public"."sys_role_permission" (
  "id" int8 NOT NULL DEFAULT nextval('sys_role_permission_id_seq'::regclass),
  "role_id" int8 NOT NULL,
  "permission_id" int8 NOT NULL,
  "deleted" int4 DEFAULT 0,
  "create_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
  "update_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP
)
;

-- ----------------------------
-- Records of sys_role_permission
-- ----------------------------

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_user";
CREATE TABLE "public"."sys_user" (
  "id" int8 NOT NULL DEFAULT nextval('sys_user_id_seq'::regclass),
  "username" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "password" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "nickname" varchar(50) COLLATE "pg_catalog"."default",
  "email" varchar(100) COLLATE "pg_catalog"."default",
  "avatar" varchar(500) COLLATE "pg_catalog"."default",
  "dept_id" int8,
  "status" int4 DEFAULT 1,
  "deleted" int4 DEFAULT 0,
  "create_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
  "update_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
  "phone" varchar(255) COLLATE "pg_catalog"."default"
)
;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO "public"."sys_user" VALUES (1, 'admin', '$2a$10$LONs5hYyrFnAvCqRkHUfkOReaXZorpi3T8K7OoIIs1NGMdoPGw5cW', '管理员', 'admin@example.com', NULL, 1, 1, 0, '2025-03-22 09:28:31.661723', '2025-03-22 09:28:31.661723', '13210001232');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_user_role";
CREATE TABLE "public"."sys_user_role" (
  "id" int8 NOT NULL DEFAULT nextval('sys_user_role_id_seq'::regclass),
  "user_id" int8 NOT NULL,
  "role_id" int8 NOT NULL,
  "deleted" int4 DEFAULT 0,
  "create_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
  "update_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP
)
;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO "public"."sys_user_role" VALUES (1, 1, 1, 1, '2025-03-22 09:28:31.667539', '2025-03-22 09:28:31.667539');
INSERT INTO "public"."sys_user_role" VALUES (1908081003758444545, 1, 1, NULL, NULL, NULL);
INSERT INTO "public"."sys_user_role" VALUES (1908081003758444546, 1, 2, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for system_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."system_config";
CREATE TABLE "public"."system_config" (
  "id" int8 NOT NULL DEFAULT nextval('system_config_id_seq'::regclass),
  "config_key" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "config_value" text COLLATE "pg_catalog"."default" NOT NULL,
  "description" varchar(500) COLLATE "pg_catalog"."default",
  "config_type" varchar(50) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "update_time" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "creator_id" int8 NOT NULL,
  "updater_id" int8
)
;
COMMENT ON COLUMN "public"."system_config"."id" IS '主键ID';
COMMENT ON COLUMN "public"."system_config"."config_key" IS '配置键';
COMMENT ON COLUMN "public"."system_config"."config_value" IS '配置值';
COMMENT ON COLUMN "public"."system_config"."description" IS '配置描述';
COMMENT ON COLUMN "public"."system_config"."config_type" IS '配置类型';
COMMENT ON COLUMN "public"."system_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."system_config"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."system_config"."creator_id" IS '创建者ID';
COMMENT ON COLUMN "public"."system_config"."updater_id" IS '更新者ID';
COMMENT ON TABLE "public"."system_config" IS '系统配置表';

-- ----------------------------
-- Records of system_config
-- ----------------------------
INSERT INTO "public"."system_config" VALUES (1, 'kb.search.type', 'EMBEDDING', '使用NLP还是EMBEDDING来处理向量化', '11', '2025-03-28 14:07:39.502642', '2025-03-28 16:03:32.747082', 1, 1);

-- ----------------------------
-- Function structure for array_to_halfvec
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."array_to_halfvec"(_numeric, int4, bool);
CREATE OR REPLACE FUNCTION "public"."array_to_halfvec"(_numeric, int4, bool)
  RETURNS "public"."halfvec" AS '$libdir/vector', 'array_to_halfvec'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for array_to_halfvec
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."array_to_halfvec"(_float8, int4, bool);
CREATE OR REPLACE FUNCTION "public"."array_to_halfvec"(_float8, int4, bool)
  RETURNS "public"."halfvec" AS '$libdir/vector', 'array_to_halfvec'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for array_to_halfvec
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."array_to_halfvec"(_float4, int4, bool);
CREATE OR REPLACE FUNCTION "public"."array_to_halfvec"(_float4, int4, bool)
  RETURNS "public"."halfvec" AS '$libdir/vector', 'array_to_halfvec'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for array_to_halfvec
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."array_to_halfvec"(_int4, int4, bool);
CREATE OR REPLACE FUNCTION "public"."array_to_halfvec"(_int4, int4, bool)
  RETURNS "public"."halfvec" AS '$libdir/vector', 'array_to_halfvec'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for array_to_sparsevec
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."array_to_sparsevec"(_int4, int4, bool);
CREATE OR REPLACE FUNCTION "public"."array_to_sparsevec"(_int4, int4, bool)
  RETURNS "public"."sparsevec" AS '$libdir/vector', 'array_to_sparsevec'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for array_to_sparsevec
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."array_to_sparsevec"(_numeric, int4, bool);
CREATE OR REPLACE FUNCTION "public"."array_to_sparsevec"(_numeric, int4, bool)
  RETURNS "public"."sparsevec" AS '$libdir/vector', 'array_to_sparsevec'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for array_to_sparsevec
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."array_to_sparsevec"(_float8, int4, bool);
CREATE OR REPLACE FUNCTION "public"."array_to_sparsevec"(_float8, int4, bool)
  RETURNS "public"."sparsevec" AS '$libdir/vector', 'array_to_sparsevec'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for array_to_sparsevec
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."array_to_sparsevec"(_float4, int4, bool);
CREATE OR REPLACE FUNCTION "public"."array_to_sparsevec"(_float4, int4, bool)
  RETURNS "public"."sparsevec" AS '$libdir/vector', 'array_to_sparsevec'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for array_to_vector
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."array_to_vector"(_float4, int4, bool);
CREATE OR REPLACE FUNCTION "public"."array_to_vector"(_float4, int4, bool)
  RETURNS "public"."vector" AS '$libdir/vector', 'array_to_vector'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for array_to_vector
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."array_to_vector"(_numeric, int4, bool);
CREATE OR REPLACE FUNCTION "public"."array_to_vector"(_numeric, int4, bool)
  RETURNS "public"."vector" AS '$libdir/vector', 'array_to_vector'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for array_to_vector
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."array_to_vector"(_float8, int4, bool);
CREATE OR REPLACE FUNCTION "public"."array_to_vector"(_float8, int4, bool)
  RETURNS "public"."vector" AS '$libdir/vector', 'array_to_vector'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for array_to_vector
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."array_to_vector"(_int4, int4, bool);
CREATE OR REPLACE FUNCTION "public"."array_to_vector"(_int4, int4, bool)
  RETURNS "public"."vector" AS '$libdir/vector', 'array_to_vector'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for binary_quantize
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."binary_quantize"("public"."halfvec");
CREATE OR REPLACE FUNCTION "public"."binary_quantize"("public"."halfvec")
  RETURNS "pg_catalog"."bit" AS '$libdir/vector', 'halfvec_binary_quantize'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for binary_quantize
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."binary_quantize"("public"."vector");
CREATE OR REPLACE FUNCTION "public"."binary_quantize"("public"."vector")
  RETURNS "pg_catalog"."bit" AS '$libdir/vector', 'binary_quantize'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for cosine_distance
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."cosine_distance"("public"."vector", "public"."vector");
CREATE OR REPLACE FUNCTION "public"."cosine_distance"("public"."vector", "public"."vector")
  RETURNS "pg_catalog"."float8" AS '$libdir/vector', 'cosine_distance'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for cosine_distance
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."cosine_distance"("public"."sparsevec", "public"."sparsevec");
CREATE OR REPLACE FUNCTION "public"."cosine_distance"("public"."sparsevec", "public"."sparsevec")
  RETURNS "pg_catalog"."float8" AS '$libdir/vector', 'sparsevec_cosine_distance'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for cosine_distance
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."cosine_distance"("public"."halfvec", "public"."halfvec");
CREATE OR REPLACE FUNCTION "public"."cosine_distance"("public"."halfvec", "public"."halfvec")
  RETURNS "pg_catalog"."float8" AS '$libdir/vector', 'halfvec_cosine_distance'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for halfvec
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."halfvec"("public"."halfvec", int4, bool);
CREATE OR REPLACE FUNCTION "public"."halfvec"("public"."halfvec", int4, bool)
  RETURNS "public"."halfvec" AS '$libdir/vector', 'halfvec'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for halfvec_accum
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."halfvec_accum"(_float8, "public"."halfvec");
CREATE OR REPLACE FUNCTION "public"."halfvec_accum"(_float8, "public"."halfvec")
  RETURNS "pg_catalog"."_float8" AS '$libdir/vector', 'halfvec_accum'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for halfvec_add
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."halfvec_add"("public"."halfvec", "public"."halfvec");
CREATE OR REPLACE FUNCTION "public"."halfvec_add"("public"."halfvec", "public"."halfvec")
  RETURNS "public"."halfvec" AS '$libdir/vector', 'halfvec_add'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for halfvec_avg
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."halfvec_avg"(_float8);
CREATE OR REPLACE FUNCTION "public"."halfvec_avg"(_float8)
  RETURNS "public"."halfvec" AS '$libdir/vector', 'halfvec_avg'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for halfvec_cmp
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."halfvec_cmp"("public"."halfvec", "public"."halfvec");
CREATE OR REPLACE FUNCTION "public"."halfvec_cmp"("public"."halfvec", "public"."halfvec")
  RETURNS "pg_catalog"."int4" AS '$libdir/vector', 'halfvec_cmp'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for halfvec_combine
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."halfvec_combine"(_float8, _float8);
CREATE OR REPLACE FUNCTION "public"."halfvec_combine"(_float8, _float8)
  RETURNS "pg_catalog"."_float8" AS '$libdir/vector', 'vector_combine'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for halfvec_concat
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."halfvec_concat"("public"."halfvec", "public"."halfvec");
CREATE OR REPLACE FUNCTION "public"."halfvec_concat"("public"."halfvec", "public"."halfvec")
  RETURNS "public"."halfvec" AS '$libdir/vector', 'halfvec_concat'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for halfvec_eq
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."halfvec_eq"("public"."halfvec", "public"."halfvec");
CREATE OR REPLACE FUNCTION "public"."halfvec_eq"("public"."halfvec", "public"."halfvec")
  RETURNS "pg_catalog"."bool" AS '$libdir/vector', 'halfvec_eq'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for halfvec_ge
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."halfvec_ge"("public"."halfvec", "public"."halfvec");
CREATE OR REPLACE FUNCTION "public"."halfvec_ge"("public"."halfvec", "public"."halfvec")
  RETURNS "pg_catalog"."bool" AS '$libdir/vector', 'halfvec_ge'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for halfvec_gt
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."halfvec_gt"("public"."halfvec", "public"."halfvec");
CREATE OR REPLACE FUNCTION "public"."halfvec_gt"("public"."halfvec", "public"."halfvec")
  RETURNS "pg_catalog"."bool" AS '$libdir/vector', 'halfvec_gt'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for halfvec_in
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."halfvec_in"(cstring, oid, int4);
CREATE OR REPLACE FUNCTION "public"."halfvec_in"(cstring, oid, int4)
  RETURNS "public"."halfvec" AS '$libdir/vector', 'halfvec_in'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for halfvec_l2_squared_distance
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."halfvec_l2_squared_distance"("public"."halfvec", "public"."halfvec");
CREATE OR REPLACE FUNCTION "public"."halfvec_l2_squared_distance"("public"."halfvec", "public"."halfvec")
  RETURNS "pg_catalog"."float8" AS '$libdir/vector', 'halfvec_l2_squared_distance'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for halfvec_le
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."halfvec_le"("public"."halfvec", "public"."halfvec");
CREATE OR REPLACE FUNCTION "public"."halfvec_le"("public"."halfvec", "public"."halfvec")
  RETURNS "pg_catalog"."bool" AS '$libdir/vector', 'halfvec_le'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for halfvec_lt
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."halfvec_lt"("public"."halfvec", "public"."halfvec");
CREATE OR REPLACE FUNCTION "public"."halfvec_lt"("public"."halfvec", "public"."halfvec")
  RETURNS "pg_catalog"."bool" AS '$libdir/vector', 'halfvec_lt'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for halfvec_mul
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."halfvec_mul"("public"."halfvec", "public"."halfvec");
CREATE OR REPLACE FUNCTION "public"."halfvec_mul"("public"."halfvec", "public"."halfvec")
  RETURNS "public"."halfvec" AS '$libdir/vector', 'halfvec_mul'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for halfvec_ne
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."halfvec_ne"("public"."halfvec", "public"."halfvec");
CREATE OR REPLACE FUNCTION "public"."halfvec_ne"("public"."halfvec", "public"."halfvec")
  RETURNS "pg_catalog"."bool" AS '$libdir/vector', 'halfvec_ne'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for halfvec_negative_inner_product
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."halfvec_negative_inner_product"("public"."halfvec", "public"."halfvec");
CREATE OR REPLACE FUNCTION "public"."halfvec_negative_inner_product"("public"."halfvec", "public"."halfvec")
  RETURNS "pg_catalog"."float8" AS '$libdir/vector', 'halfvec_negative_inner_product'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for halfvec_out
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."halfvec_out"("public"."halfvec");
CREATE OR REPLACE FUNCTION "public"."halfvec_out"("public"."halfvec")
  RETURNS "pg_catalog"."cstring" AS '$libdir/vector', 'halfvec_out'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for halfvec_recv
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."halfvec_recv"(internal, oid, int4);
CREATE OR REPLACE FUNCTION "public"."halfvec_recv"(internal, oid, int4)
  RETURNS "public"."halfvec" AS '$libdir/vector', 'halfvec_recv'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for halfvec_send
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."halfvec_send"("public"."halfvec");
CREATE OR REPLACE FUNCTION "public"."halfvec_send"("public"."halfvec")
  RETURNS "pg_catalog"."bytea" AS '$libdir/vector', 'halfvec_send'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for halfvec_spherical_distance
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."halfvec_spherical_distance"("public"."halfvec", "public"."halfvec");
CREATE OR REPLACE FUNCTION "public"."halfvec_spherical_distance"("public"."halfvec", "public"."halfvec")
  RETURNS "pg_catalog"."float8" AS '$libdir/vector', 'halfvec_spherical_distance'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for halfvec_sub
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."halfvec_sub"("public"."halfvec", "public"."halfvec");
CREATE OR REPLACE FUNCTION "public"."halfvec_sub"("public"."halfvec", "public"."halfvec")
  RETURNS "public"."halfvec" AS '$libdir/vector', 'halfvec_sub'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for halfvec_to_float4
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."halfvec_to_float4"("public"."halfvec", int4, bool);
CREATE OR REPLACE FUNCTION "public"."halfvec_to_float4"("public"."halfvec", int4, bool)
  RETURNS "pg_catalog"."_float4" AS '$libdir/vector', 'halfvec_to_float4'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for halfvec_to_sparsevec
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."halfvec_to_sparsevec"("public"."halfvec", int4, bool);
CREATE OR REPLACE FUNCTION "public"."halfvec_to_sparsevec"("public"."halfvec", int4, bool)
  RETURNS "public"."sparsevec" AS '$libdir/vector', 'halfvec_to_sparsevec'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for halfvec_to_vector
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."halfvec_to_vector"("public"."halfvec", int4, bool);
CREATE OR REPLACE FUNCTION "public"."halfvec_to_vector"("public"."halfvec", int4, bool)
  RETURNS "public"."vector" AS '$libdir/vector', 'halfvec_to_vector'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for halfvec_typmod_in
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."halfvec_typmod_in"(_cstring);
CREATE OR REPLACE FUNCTION "public"."halfvec_typmod_in"(_cstring)
  RETURNS "pg_catalog"."int4" AS '$libdir/vector', 'halfvec_typmod_in'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for hamming_distance
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."hamming_distance"(bit, bit);
CREATE OR REPLACE FUNCTION "public"."hamming_distance"(bit, bit)
  RETURNS "pg_catalog"."float8" AS '$libdir/vector', 'hamming_distance'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for hnsw_bit_support
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."hnsw_bit_support"(internal);
CREATE OR REPLACE FUNCTION "public"."hnsw_bit_support"(internal)
  RETURNS "pg_catalog"."internal" AS '$libdir/vector', 'hnsw_bit_support'
  LANGUAGE c VOLATILE
  COST 1;

-- ----------------------------
-- Function structure for hnsw_halfvec_support
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."hnsw_halfvec_support"(internal);
CREATE OR REPLACE FUNCTION "public"."hnsw_halfvec_support"(internal)
  RETURNS "pg_catalog"."internal" AS '$libdir/vector', 'hnsw_halfvec_support'
  LANGUAGE c VOLATILE
  COST 1;

-- ----------------------------
-- Function structure for hnsw_sparsevec_support
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."hnsw_sparsevec_support"(internal);
CREATE OR REPLACE FUNCTION "public"."hnsw_sparsevec_support"(internal)
  RETURNS "pg_catalog"."internal" AS '$libdir/vector', 'hnsw_sparsevec_support'
  LANGUAGE c VOLATILE
  COST 1;

-- ----------------------------
-- Function structure for hnswhandler
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."hnswhandler"(internal);
CREATE OR REPLACE FUNCTION "public"."hnswhandler"(internal)
  RETURNS "pg_catalog"."index_am_handler" AS '$libdir/vector', 'hnswhandler'
  LANGUAGE c VOLATILE
  COST 1;

-- ----------------------------
-- Function structure for inner_product
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."inner_product"("public"."sparsevec", "public"."sparsevec");
CREATE OR REPLACE FUNCTION "public"."inner_product"("public"."sparsevec", "public"."sparsevec")
  RETURNS "pg_catalog"."float8" AS '$libdir/vector', 'sparsevec_inner_product'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for inner_product
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."inner_product"("public"."vector", "public"."vector");
CREATE OR REPLACE FUNCTION "public"."inner_product"("public"."vector", "public"."vector")
  RETURNS "pg_catalog"."float8" AS '$libdir/vector', 'inner_product'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for inner_product
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."inner_product"("public"."halfvec", "public"."halfvec");
CREATE OR REPLACE FUNCTION "public"."inner_product"("public"."halfvec", "public"."halfvec")
  RETURNS "pg_catalog"."float8" AS '$libdir/vector', 'halfvec_inner_product'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for ivfflat_bit_support
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."ivfflat_bit_support"(internal);
CREATE OR REPLACE FUNCTION "public"."ivfflat_bit_support"(internal)
  RETURNS "pg_catalog"."internal" AS '$libdir/vector', 'ivfflat_bit_support'
  LANGUAGE c VOLATILE
  COST 1;

-- ----------------------------
-- Function structure for ivfflat_halfvec_support
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."ivfflat_halfvec_support"(internal);
CREATE OR REPLACE FUNCTION "public"."ivfflat_halfvec_support"(internal)
  RETURNS "pg_catalog"."internal" AS '$libdir/vector', 'ivfflat_halfvec_support'
  LANGUAGE c VOLATILE
  COST 1;

-- ----------------------------
-- Function structure for ivfflathandler
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."ivfflathandler"(internal);
CREATE OR REPLACE FUNCTION "public"."ivfflathandler"(internal)
  RETURNS "pg_catalog"."index_am_handler" AS '$libdir/vector', 'ivfflathandler'
  LANGUAGE c VOLATILE
  COST 1;

-- ----------------------------
-- Function structure for jaccard_distance
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."jaccard_distance"(bit, bit);
CREATE OR REPLACE FUNCTION "public"."jaccard_distance"(bit, bit)
  RETURNS "pg_catalog"."float8" AS '$libdir/vector', 'jaccard_distance'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for l1_distance
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."l1_distance"("public"."halfvec", "public"."halfvec");
CREATE OR REPLACE FUNCTION "public"."l1_distance"("public"."halfvec", "public"."halfvec")
  RETURNS "pg_catalog"."float8" AS '$libdir/vector', 'halfvec_l1_distance'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for l1_distance
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."l1_distance"("public"."sparsevec", "public"."sparsevec");
CREATE OR REPLACE FUNCTION "public"."l1_distance"("public"."sparsevec", "public"."sparsevec")
  RETURNS "pg_catalog"."float8" AS '$libdir/vector', 'sparsevec_l1_distance'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for l1_distance
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."l1_distance"("public"."vector", "public"."vector");
CREATE OR REPLACE FUNCTION "public"."l1_distance"("public"."vector", "public"."vector")
  RETURNS "pg_catalog"."float8" AS '$libdir/vector', 'l1_distance'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for l2_distance
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."l2_distance"("public"."vector", "public"."vector");
CREATE OR REPLACE FUNCTION "public"."l2_distance"("public"."vector", "public"."vector")
  RETURNS "pg_catalog"."float8" AS '$libdir/vector', 'l2_distance'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for l2_distance
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."l2_distance"("public"."sparsevec", "public"."sparsevec");
CREATE OR REPLACE FUNCTION "public"."l2_distance"("public"."sparsevec", "public"."sparsevec")
  RETURNS "pg_catalog"."float8" AS '$libdir/vector', 'sparsevec_l2_distance'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for l2_distance
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."l2_distance"("public"."halfvec", "public"."halfvec");
CREATE OR REPLACE FUNCTION "public"."l2_distance"("public"."halfvec", "public"."halfvec")
  RETURNS "pg_catalog"."float8" AS '$libdir/vector', 'halfvec_l2_distance'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for l2_norm
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."l2_norm"("public"."halfvec");
CREATE OR REPLACE FUNCTION "public"."l2_norm"("public"."halfvec")
  RETURNS "pg_catalog"."float8" AS '$libdir/vector', 'halfvec_l2_norm'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for l2_norm
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."l2_norm"("public"."sparsevec");
CREATE OR REPLACE FUNCTION "public"."l2_norm"("public"."sparsevec")
  RETURNS "pg_catalog"."float8" AS '$libdir/vector', 'sparsevec_l2_norm'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for l2_normalize
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."l2_normalize"("public"."halfvec");
CREATE OR REPLACE FUNCTION "public"."l2_normalize"("public"."halfvec")
  RETURNS "public"."halfvec" AS '$libdir/vector', 'halfvec_l2_normalize'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for l2_normalize
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."l2_normalize"("public"."sparsevec");
CREATE OR REPLACE FUNCTION "public"."l2_normalize"("public"."sparsevec")
  RETURNS "public"."sparsevec" AS '$libdir/vector', 'sparsevec_l2_normalize'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for l2_normalize
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."l2_normalize"("public"."vector");
CREATE OR REPLACE FUNCTION "public"."l2_normalize"("public"."vector")
  RETURNS "public"."vector" AS '$libdir/vector', 'l2_normalize'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for sparsevec
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."sparsevec"("public"."sparsevec", int4, bool);
CREATE OR REPLACE FUNCTION "public"."sparsevec"("public"."sparsevec", int4, bool)
  RETURNS "public"."sparsevec" AS '$libdir/vector', 'sparsevec'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for sparsevec_cmp
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."sparsevec_cmp"("public"."sparsevec", "public"."sparsevec");
CREATE OR REPLACE FUNCTION "public"."sparsevec_cmp"("public"."sparsevec", "public"."sparsevec")
  RETURNS "pg_catalog"."int4" AS '$libdir/vector', 'sparsevec_cmp'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for sparsevec_eq
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."sparsevec_eq"("public"."sparsevec", "public"."sparsevec");
CREATE OR REPLACE FUNCTION "public"."sparsevec_eq"("public"."sparsevec", "public"."sparsevec")
  RETURNS "pg_catalog"."bool" AS '$libdir/vector', 'sparsevec_eq'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for sparsevec_ge
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."sparsevec_ge"("public"."sparsevec", "public"."sparsevec");
CREATE OR REPLACE FUNCTION "public"."sparsevec_ge"("public"."sparsevec", "public"."sparsevec")
  RETURNS "pg_catalog"."bool" AS '$libdir/vector', 'sparsevec_ge'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for sparsevec_gt
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."sparsevec_gt"("public"."sparsevec", "public"."sparsevec");
CREATE OR REPLACE FUNCTION "public"."sparsevec_gt"("public"."sparsevec", "public"."sparsevec")
  RETURNS "pg_catalog"."bool" AS '$libdir/vector', 'sparsevec_gt'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for sparsevec_in
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."sparsevec_in"(cstring, oid, int4);
CREATE OR REPLACE FUNCTION "public"."sparsevec_in"(cstring, oid, int4)
  RETURNS "public"."sparsevec" AS '$libdir/vector', 'sparsevec_in'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for sparsevec_l2_squared_distance
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."sparsevec_l2_squared_distance"("public"."sparsevec", "public"."sparsevec");
CREATE OR REPLACE FUNCTION "public"."sparsevec_l2_squared_distance"("public"."sparsevec", "public"."sparsevec")
  RETURNS "pg_catalog"."float8" AS '$libdir/vector', 'sparsevec_l2_squared_distance'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for sparsevec_le
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."sparsevec_le"("public"."sparsevec", "public"."sparsevec");
CREATE OR REPLACE FUNCTION "public"."sparsevec_le"("public"."sparsevec", "public"."sparsevec")
  RETURNS "pg_catalog"."bool" AS '$libdir/vector', 'sparsevec_le'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for sparsevec_lt
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."sparsevec_lt"("public"."sparsevec", "public"."sparsevec");
CREATE OR REPLACE FUNCTION "public"."sparsevec_lt"("public"."sparsevec", "public"."sparsevec")
  RETURNS "pg_catalog"."bool" AS '$libdir/vector', 'sparsevec_lt'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for sparsevec_ne
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."sparsevec_ne"("public"."sparsevec", "public"."sparsevec");
CREATE OR REPLACE FUNCTION "public"."sparsevec_ne"("public"."sparsevec", "public"."sparsevec")
  RETURNS "pg_catalog"."bool" AS '$libdir/vector', 'sparsevec_ne'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for sparsevec_negative_inner_product
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."sparsevec_negative_inner_product"("public"."sparsevec", "public"."sparsevec");
CREATE OR REPLACE FUNCTION "public"."sparsevec_negative_inner_product"("public"."sparsevec", "public"."sparsevec")
  RETURNS "pg_catalog"."float8" AS '$libdir/vector', 'sparsevec_negative_inner_product'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for sparsevec_out
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."sparsevec_out"("public"."sparsevec");
CREATE OR REPLACE FUNCTION "public"."sparsevec_out"("public"."sparsevec")
  RETURNS "pg_catalog"."cstring" AS '$libdir/vector', 'sparsevec_out'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for sparsevec_recv
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."sparsevec_recv"(internal, oid, int4);
CREATE OR REPLACE FUNCTION "public"."sparsevec_recv"(internal, oid, int4)
  RETURNS "public"."sparsevec" AS '$libdir/vector', 'sparsevec_recv'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for sparsevec_send
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."sparsevec_send"("public"."sparsevec");
CREATE OR REPLACE FUNCTION "public"."sparsevec_send"("public"."sparsevec")
  RETURNS "pg_catalog"."bytea" AS '$libdir/vector', 'sparsevec_send'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for sparsevec_to_halfvec
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."sparsevec_to_halfvec"("public"."sparsevec", int4, bool);
CREATE OR REPLACE FUNCTION "public"."sparsevec_to_halfvec"("public"."sparsevec", int4, bool)
  RETURNS "public"."halfvec" AS '$libdir/vector', 'sparsevec_to_halfvec'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for sparsevec_to_vector
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."sparsevec_to_vector"("public"."sparsevec", int4, bool);
CREATE OR REPLACE FUNCTION "public"."sparsevec_to_vector"("public"."sparsevec", int4, bool)
  RETURNS "public"."vector" AS '$libdir/vector', 'sparsevec_to_vector'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for sparsevec_typmod_in
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."sparsevec_typmod_in"(_cstring);
CREATE OR REPLACE FUNCTION "public"."sparsevec_typmod_in"(_cstring)
  RETURNS "pg_catalog"."int4" AS '$libdir/vector', 'sparsevec_typmod_in'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for subvector
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."subvector"("public"."vector", int4, int4);
CREATE OR REPLACE FUNCTION "public"."subvector"("public"."vector", int4, int4)
  RETURNS "public"."vector" AS '$libdir/vector', 'subvector'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for subvector
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."subvector"("public"."halfvec", int4, int4);
CREATE OR REPLACE FUNCTION "public"."subvector"("public"."halfvec", int4, int4)
  RETURNS "public"."halfvec" AS '$libdir/vector', 'halfvec_subvector'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for update_updated_at_column
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."update_updated_at_column"();
CREATE OR REPLACE FUNCTION "public"."update_updated_at_column"()
  RETURNS "pg_catalog"."trigger" AS $BODY$
BEGIN
    NEW.update_time = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

-- ----------------------------
-- Function structure for vector
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."vector"("public"."vector", int4, bool);
CREATE OR REPLACE FUNCTION "public"."vector"("public"."vector", int4, bool)
  RETURNS "public"."vector" AS '$libdir/vector', 'vector'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for vector_accum
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."vector_accum"(_float8, "public"."vector");
CREATE OR REPLACE FUNCTION "public"."vector_accum"(_float8, "public"."vector")
  RETURNS "pg_catalog"."_float8" AS '$libdir/vector', 'vector_accum'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for vector_add
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."vector_add"("public"."vector", "public"."vector");
CREATE OR REPLACE FUNCTION "public"."vector_add"("public"."vector", "public"."vector")
  RETURNS "public"."vector" AS '$libdir/vector', 'vector_add'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for vector_avg
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."vector_avg"(_float8);
CREATE OR REPLACE FUNCTION "public"."vector_avg"(_float8)
  RETURNS "public"."vector" AS '$libdir/vector', 'vector_avg'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for vector_cmp
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."vector_cmp"("public"."vector", "public"."vector");
CREATE OR REPLACE FUNCTION "public"."vector_cmp"("public"."vector", "public"."vector")
  RETURNS "pg_catalog"."int4" AS '$libdir/vector', 'vector_cmp'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for vector_combine
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."vector_combine"(_float8, _float8);
CREATE OR REPLACE FUNCTION "public"."vector_combine"(_float8, _float8)
  RETURNS "pg_catalog"."_float8" AS '$libdir/vector', 'vector_combine'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for vector_concat
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."vector_concat"("public"."vector", "public"."vector");
CREATE OR REPLACE FUNCTION "public"."vector_concat"("public"."vector", "public"."vector")
  RETURNS "public"."vector" AS '$libdir/vector', 'vector_concat'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for vector_dims
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."vector_dims"("public"."halfvec");
CREATE OR REPLACE FUNCTION "public"."vector_dims"("public"."halfvec")
  RETURNS "pg_catalog"."int4" AS '$libdir/vector', 'halfvec_vector_dims'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for vector_dims
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."vector_dims"("public"."vector");
CREATE OR REPLACE FUNCTION "public"."vector_dims"("public"."vector")
  RETURNS "pg_catalog"."int4" AS '$libdir/vector', 'vector_dims'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for vector_eq
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."vector_eq"("public"."vector", "public"."vector");
CREATE OR REPLACE FUNCTION "public"."vector_eq"("public"."vector", "public"."vector")
  RETURNS "pg_catalog"."bool" AS '$libdir/vector', 'vector_eq'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for vector_ge
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."vector_ge"("public"."vector", "public"."vector");
CREATE OR REPLACE FUNCTION "public"."vector_ge"("public"."vector", "public"."vector")
  RETURNS "pg_catalog"."bool" AS '$libdir/vector', 'vector_ge'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for vector_gt
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."vector_gt"("public"."vector", "public"."vector");
CREATE OR REPLACE FUNCTION "public"."vector_gt"("public"."vector", "public"."vector")
  RETURNS "pg_catalog"."bool" AS '$libdir/vector', 'vector_gt'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for vector_in
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."vector_in"(cstring, oid, int4);
CREATE OR REPLACE FUNCTION "public"."vector_in"(cstring, oid, int4)
  RETURNS "public"."vector" AS '$libdir/vector', 'vector_in'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for vector_l2_squared_distance
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."vector_l2_squared_distance"("public"."vector", "public"."vector");
CREATE OR REPLACE FUNCTION "public"."vector_l2_squared_distance"("public"."vector", "public"."vector")
  RETURNS "pg_catalog"."float8" AS '$libdir/vector', 'vector_l2_squared_distance'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for vector_le
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."vector_le"("public"."vector", "public"."vector");
CREATE OR REPLACE FUNCTION "public"."vector_le"("public"."vector", "public"."vector")
  RETURNS "pg_catalog"."bool" AS '$libdir/vector', 'vector_le'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for vector_lt
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."vector_lt"("public"."vector", "public"."vector");
CREATE OR REPLACE FUNCTION "public"."vector_lt"("public"."vector", "public"."vector")
  RETURNS "pg_catalog"."bool" AS '$libdir/vector', 'vector_lt'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for vector_mul
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."vector_mul"("public"."vector", "public"."vector");
CREATE OR REPLACE FUNCTION "public"."vector_mul"("public"."vector", "public"."vector")
  RETURNS "public"."vector" AS '$libdir/vector', 'vector_mul'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for vector_ne
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."vector_ne"("public"."vector", "public"."vector");
CREATE OR REPLACE FUNCTION "public"."vector_ne"("public"."vector", "public"."vector")
  RETURNS "pg_catalog"."bool" AS '$libdir/vector', 'vector_ne'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for vector_negative_inner_product
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."vector_negative_inner_product"("public"."vector", "public"."vector");
CREATE OR REPLACE FUNCTION "public"."vector_negative_inner_product"("public"."vector", "public"."vector")
  RETURNS "pg_catalog"."float8" AS '$libdir/vector', 'vector_negative_inner_product'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for vector_norm
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."vector_norm"("public"."vector");
CREATE OR REPLACE FUNCTION "public"."vector_norm"("public"."vector")
  RETURNS "pg_catalog"."float8" AS '$libdir/vector', 'vector_norm'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for vector_out
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."vector_out"("public"."vector");
CREATE OR REPLACE FUNCTION "public"."vector_out"("public"."vector")
  RETURNS "pg_catalog"."cstring" AS '$libdir/vector', 'vector_out'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for vector_recv
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."vector_recv"(internal, oid, int4);
CREATE OR REPLACE FUNCTION "public"."vector_recv"(internal, oid, int4)
  RETURNS "public"."vector" AS '$libdir/vector', 'vector_recv'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for vector_send
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."vector_send"("public"."vector");
CREATE OR REPLACE FUNCTION "public"."vector_send"("public"."vector")
  RETURNS "pg_catalog"."bytea" AS '$libdir/vector', 'vector_send'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for vector_spherical_distance
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."vector_spherical_distance"("public"."vector", "public"."vector");
CREATE OR REPLACE FUNCTION "public"."vector_spherical_distance"("public"."vector", "public"."vector")
  RETURNS "pg_catalog"."float8" AS '$libdir/vector', 'vector_spherical_distance'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for vector_sub
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."vector_sub"("public"."vector", "public"."vector");
CREATE OR REPLACE FUNCTION "public"."vector_sub"("public"."vector", "public"."vector")
  RETURNS "public"."vector" AS '$libdir/vector', 'vector_sub'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for vector_to_float4
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."vector_to_float4"("public"."vector", int4, bool);
CREATE OR REPLACE FUNCTION "public"."vector_to_float4"("public"."vector", int4, bool)
  RETURNS "pg_catalog"."_float4" AS '$libdir/vector', 'vector_to_float4'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for vector_to_halfvec
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."vector_to_halfvec"("public"."vector", int4, bool);
CREATE OR REPLACE FUNCTION "public"."vector_to_halfvec"("public"."vector", int4, bool)
  RETURNS "public"."halfvec" AS '$libdir/vector', 'vector_to_halfvec'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for vector_to_sparsevec
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."vector_to_sparsevec"("public"."vector", int4, bool);
CREATE OR REPLACE FUNCTION "public"."vector_to_sparsevec"("public"."vector", int4, bool)
  RETURNS "public"."sparsevec" AS '$libdir/vector', 'vector_to_sparsevec'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for vector_typmod_in
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."vector_typmod_in"(_cstring);
CREATE OR REPLACE FUNCTION "public"."vector_typmod_in"(_cstring)
  RETURNS "pg_catalog"."int4" AS '$libdir/vector', 'vector_typmod_in'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."embedding_config_id_seq"
OWNED BY "public"."embedding_config"."id";
SELECT setval('"public"."embedding_config_id_seq"', 15, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."kb_category_id_seq"
OWNED BY "public"."kb_category"."id";
SELECT setval('"public"."kb_category_id_seq"', 23, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."kb_chat_history_id_seq"
OWNED BY "public"."kb_chat_history"."id";
SELECT setval('"public"."kb_chat_history_id_seq"', 169, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."kb_document_id_seq"
OWNED BY "public"."kb_document"."id";
SELECT setval('"public"."kb_document_id_seq"', 44, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."kb_document_tag_id_seq"
OWNED BY "public"."kb_document_tag"."id";
SELECT setval('"public"."kb_document_tag_id_seq"', 8, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."kb_document_vector_id_seq"
OWNED BY "public"."kb_document_vector"."id";
SELECT setval('"public"."kb_document_vector_id_seq"', 75, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."kb_document_version_id_seq"
OWNED BY "public"."kb_document_version"."id";
SELECT setval('"public"."kb_document_version_id_seq"', 44, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."kb_knowledge_base_id_seq"
OWNED BY "public"."kb_knowledge_base"."id";
SELECT setval('"public"."kb_knowledge_base_id_seq"', 32, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."kb_knowledge_base_permission_id_seq"
OWNED BY "public"."kb_knowledge_base_permission"."id";
SELECT setval('"public"."kb_knowledge_base_permission_id_seq"', 32, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."kb_tag_id_seq"
OWNED BY "public"."kb_tag"."id";
SELECT setval('"public"."kb_tag_id_seq"', 23, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."kb_user_behavior_id_seq"
OWNED BY "public"."kb_user_behavior"."id";
SELECT setval('"public"."kb_user_behavior_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."llm_config_id_seq"
OWNED BY "public"."llm_config"."id";
SELECT setval('"public"."llm_config_id_seq"', 7, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."llm_token_usage_id_seq"
OWNED BY "public"."llm_token_usage"."id";
SELECT setval('"public"."llm_token_usage_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_ai_model_id_seq"
OWNED BY "public"."sys_ai_model"."id";
SELECT setval('"public"."sys_ai_model_id_seq"', 1, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_dept_id_seq"
OWNED BY "public"."sys_dept"."id";
SELECT setval('"public"."sys_dept_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_permission_id_seq"
OWNED BY "public"."sys_permission"."id";
SELECT setval('"public"."sys_permission_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_role_id_seq"
OWNED BY "public"."sys_role"."id";
SELECT setval('"public"."sys_role_id_seq"', 1, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_role_permission_id_seq"
OWNED BY "public"."sys_role_permission"."id";
SELECT setval('"public"."sys_role_permission_id_seq"', 15, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_user_id_seq"
OWNED BY "public"."sys_user"."id";
SELECT setval('"public"."sys_user_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_user_role_id_seq"
OWNED BY "public"."sys_user_role"."id";
SELECT setval('"public"."sys_user_role_id_seq"', 1, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."system_config_id_seq"
OWNED BY "public"."system_config"."id";
SELECT setval('"public"."system_config_id_seq"', 11, true);

-- ----------------------------
-- Triggers structure for table embedding_config
-- ----------------------------
CREATE TRIGGER "update_embedding_config_updated_at" BEFORE UPDATE ON "public"."embedding_config"
FOR EACH ROW
EXECUTE PROCEDURE "public"."update_updated_at_column"();

-- ----------------------------
-- Uniques structure for table embedding_config
-- ----------------------------
ALTER TABLE "public"."embedding_config" ADD CONSTRAINT "uk_model_type" UNIQUE ("model_type");

-- ----------------------------
-- Primary Key structure for table embedding_config
-- ----------------------------
ALTER TABLE "public"."embedding_config" ADD CONSTRAINT "embedding_config_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table kb_category
-- ----------------------------
CREATE INDEX "idx_kb_category_kb_id" ON "public"."kb_category" USING btree (
  "kb_id" "pg_catalog"."int8_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table kb_category
-- ----------------------------
ALTER TABLE "public"."kb_category" ADD CONSTRAINT "kb_category_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table kb_chat_history
-- ----------------------------
CREATE INDEX "idx_kb_chat_history_kb_id" ON "public"."kb_chat_history" USING btree (
  "kb_ids" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table kb_chat_history
-- ----------------------------
ALTER TABLE "public"."kb_chat_history" ADD CONSTRAINT "kb_chat_history_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table kb_document
-- ----------------------------
CREATE INDEX "idx_kb_document_kb_id" ON "public"."kb_document" USING btree (
  "kb_id" "pg_catalog"."int8_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table kb_document
-- ----------------------------
ALTER TABLE "public"."kb_document" ADD CONSTRAINT "kb_document_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table kb_document_tag
-- ----------------------------
CREATE INDEX "idx_kb_document_tag_doc_id" ON "public"."kb_document_tag" USING btree (
  "document_id" "pg_catalog"."int8_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table kb_document_tag
-- ----------------------------
ALTER TABLE "public"."kb_document_tag" ADD CONSTRAINT "kb_document_tag_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table kb_document_vector
-- ----------------------------
CREATE INDEX "idx_kb_document_vector_doc_id" ON "public"."kb_document_vector" USING btree (
  "document_id" "pg_catalog"."int8_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table kb_document_vector
-- ----------------------------
ALTER TABLE "public"."kb_document_vector" ADD CONSTRAINT "kb_document_vector_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table kb_document_version
-- ----------------------------
CREATE INDEX "idx_kb_document_version_doc_id" ON "public"."kb_document_version" USING btree (
  "document_id" "pg_catalog"."int8_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table kb_document_version
-- ----------------------------
ALTER TABLE "public"."kb_document_version" ADD CONSTRAINT "kb_document_version_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table kb_knowledge_base
-- ----------------------------
ALTER TABLE "public"."kb_knowledge_base" ADD CONSTRAINT "kb_knowledge_base_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table kb_knowledge_base_permission
-- ----------------------------
CREATE INDEX "idx_kb_permission_kb_id" ON "public"."kb_knowledge_base_permission" USING btree (
  "kb_id" "pg_catalog"."int8_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table kb_knowledge_base_permission
-- ----------------------------
ALTER TABLE "public"."kb_knowledge_base_permission" ADD CONSTRAINT "kb_knowledge_base_permission_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table kb_tag
-- ----------------------------
CREATE INDEX "idx_kb_tag_kb_id" ON "public"."kb_tag" USING btree (
  "kb_id" "pg_catalog"."int8_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table kb_tag
-- ----------------------------
ALTER TABLE "public"."kb_tag" ADD CONSTRAINT "kb_tag_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table kb_user_behavior
-- ----------------------------
CREATE INDEX "idx_kb_user_behavior_kb_id" ON "public"."kb_user_behavior" USING btree (
  "kb_id" "pg_catalog"."int8_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table kb_user_behavior
-- ----------------------------
ALTER TABLE "public"."kb_user_behavior" ADD CONSTRAINT "kb_user_behavior_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table llm_config
-- ----------------------------
ALTER TABLE "public"."llm_config" ADD CONSTRAINT "llm_config_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table llm_token_usage
-- ----------------------------
ALTER TABLE "public"."llm_token_usage" ADD CONSTRAINT "llm_token_usage_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table sys_ai_model
-- ----------------------------
ALTER TABLE "public"."sys_ai_model" ADD CONSTRAINT "sys_ai_model_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table sys_dept
-- ----------------------------
ALTER TABLE "public"."sys_dept" ADD CONSTRAINT "sys_dept_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Uniques structure for table sys_permission
-- ----------------------------
ALTER TABLE "public"."sys_permission" ADD CONSTRAINT "uk_permission_code" UNIQUE ("permission_code");

-- ----------------------------
-- Primary Key structure for table sys_permission
-- ----------------------------
ALTER TABLE "public"."sys_permission" ADD CONSTRAINT "sys_permission_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Uniques structure for table sys_role
-- ----------------------------
ALTER TABLE "public"."sys_role" ADD CONSTRAINT "uk_role_code" UNIQUE ("role_code");

-- ----------------------------
-- Primary Key structure for table sys_role
-- ----------------------------
ALTER TABLE "public"."sys_role" ADD CONSTRAINT "sys_role_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table sys_role_permission
-- ----------------------------
CREATE INDEX "idx_sys_role_permission_role_id" ON "public"."sys_role_permission" USING btree (
  "role_id" "pg_catalog"."int8_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table sys_role_permission
-- ----------------------------
ALTER TABLE "public"."sys_role_permission" ADD CONSTRAINT "sys_role_permission_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Uniques structure for table sys_user
-- ----------------------------
ALTER TABLE "public"."sys_user" ADD CONSTRAINT "uk_username" UNIQUE ("username");

-- ----------------------------
-- Primary Key structure for table sys_user
-- ----------------------------
ALTER TABLE "public"."sys_user" ADD CONSTRAINT "sys_user_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table sys_user_role
-- ----------------------------
CREATE INDEX "idx_sys_user_role_user_id" ON "public"."sys_user_role" USING btree (
  "user_id" "pg_catalog"."int8_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table sys_user_role
-- ----------------------------
ALTER TABLE "public"."sys_user_role" ADD CONSTRAINT "sys_user_role_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Triggers structure for table system_config
-- ----------------------------
CREATE TRIGGER "update_system_config_updated_at" BEFORE UPDATE ON "public"."system_config"
FOR EACH ROW
EXECUTE PROCEDURE "public"."update_updated_at_column"();

-- ----------------------------
-- Uniques structure for table system_config
-- ----------------------------
ALTER TABLE "public"."system_config" ADD CONSTRAINT "uk_config_key" UNIQUE ("config_key");

-- ----------------------------
-- Primary Key structure for table system_config
-- ----------------------------
ALTER TABLE "public"."system_config" ADD CONSTRAINT "system_config_pkey" PRIMARY KEY ("id");
