# 🎓 Real Challenge – Backend (ADS UNIFAA - 4º Semestre)

Este repositório contém o **backend** do projeto desenvolvido para o **Real Challenge (ODS 4 – Educação de Qualidade)**, disciplina de Análise e Desenvolvimento de Sistemas – UNIFAA.

---

## 📌 Tecnologias Utilizadas
- **Java 21**
- **Spring Boot 3.5.6**
  - Spring Web
  - Spring Data JPA
  - Spring Validation
- **Maven**
- **Supabase (PostgreSQL na nuvem)**
- **Lombok**
- **Git/GitHub**

---

## 🚀 Como rodar o projeto

### ✅ Pré-requisitos
- JDK **21**
- Maven **3.9+**
- IDE (IntelliJ IDEA, Eclipse ou VSCode com extensões Java)
- Conta no [Supabase](https://supabase.com) e credenciais de banco PostgreSQL

### ▶️ Passos
1. Clone o repositório:
   git clone https://github.com/Fabiaudi/ADS-UNIFAA-4-semestre-RealChallenge-ADS-BACKEND.git
   cd ADS-UNIFAA-4-semestre-RealChallenge-ADS-BACKEND

2. Configure o banco de dados no arquivo `src/main/resources/application.properties`:

   ```properties
   spring.datasource.url=jdbc:postgresql://<host>:<port>/<database>
   spring.datasource.username=<usuario>
   spring.datasource.password=<senha>

   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
   ```

   > 🔑 As credenciais estão disponíveis no painel do Supabase em **Project Settings → Database**.

3. Compile e rode o projeto:

   ```bash
   mvn spring-boot:run
   ```

4. Acesse no navegador:

   ```
   http://localhost:8080
   ```

---

## 🌿 Fluxo de Branches

Organização do time com branches individuais:

* **main** → branch estável, somente com código testado e funcional.
* **dev/nome** → branch pessoal de cada desenvolvedor.

### Criando sua branch:

```bash
git checkout main
git pull origin main
git checkout -b dev/seu-nome
git push -u origin dev/seu-nome
```

### Fluxo de trabalho:

1. Cada dev trabalha apenas em sua branch (`dev/fabi`, `dev/lais`, etc.).
2. Commits seguem o padrão [Conventional Commits](https://www.conventionalcommits.org/):

   * `feat:` nova funcionalidade
   * `fix:` correção de bug
   * `chore:` ajustes menores (docs, configs)
3. Para integrar na `main`:

   * Via **Pull Request** no GitHub (preferido).
   * Ou manualmente:

     ```bash
     git checkout main
     git pull origin main
     git merge dev/seu-nome
     git push origin main
     ```

---

## 📂 Estrutura do Projeto

```
src/
 ├── main/
     ├── java/com/unifaa/realchallenge/   # código fonte (controllers, services, repositories)
     └── resources/                       # configs (application.properties, static, templates)
 
```

---

## 👩‍💻 Equipe

* Fabiana Audi
* Laís Brum
* Nathália Oliveira
* Vitor Leal
* Lorenzo Gabriel Almeida
* Lucas Gentil
* Helton Fialho

---

## 📄 Licença

Projeto acadêmico, uso restrito à disciplina **Real Challenge – UNIFAA**.

