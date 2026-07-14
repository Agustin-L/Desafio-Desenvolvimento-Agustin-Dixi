# Projeto Dixi — Sistema de Funcionários

Sistema de gestão de Funcionários, Cargos, Departamentos e Vínculos empregatícios, com login e controle de acesso.

## Stack

- **Backend:** Java 17, Spring Boot 3.2.5 (Web, Data JPA, Validation, Security), JWT (JJWT), banco H2 persistido em arquivo
- **Frontend:** React 18 + Vite, React Router, Axios

## Estrutura

- `backend/` — API REST (`/api/auth`, `/api/usuarios`, `/api/funcionarios`, `/api/cargos`, `/api/departamentos`, `/api/vinculos`, `/api/relatorios`)
- `frontend/` — SPA em React
- `docker-compose.yml` — sobe backend (porta 8080) e frontend (porta 5173)

## Login e controle de acesso

O sistema agora exige login (JWT) para usar a API e o frontend.

- Ao subir a aplicação **pela primeira vez**, é criado automaticamente um usuário administrador padrão:
  - **Usuário:** `admin`
  - **Senha:** `admin123`
  - (fica registrado no log do backend na inicialização)
- Existem dois perfis de acesso:
  - **ADMIN** — acessa tudo, inclusive a tela de **Usuários**, onde pode **criar** novos acessos (perfil Admin ou Padrão) e **excluir** acessos existentes.
  - **PADRAO** — acessa as telas de Funcionários, Cargos, Departamentos e Vínculos, mas não vê nem consegue chamar a área de gestão de usuários (bloqueado tanto na tela quanto na API).
- Regras de exclusão: um usuário **não pode excluir o próprio acesso** (evita ficar sem nenhum admin por engano). Só quem tem perfil ADMIN pode excluir outros usuários.
- **Recomendação:** troque a senha do `admin` padrão assim que possível — crie um novo usuário administrador com uma senha forte pela tela de Usuários e, se quiser, remova/recrie o `admin` original.

## Sobre o banco de dados

O projeto original usava H2 **em memória** (`jdbc:h2:mem:...`), então todo o cadastro (e os usuários) eram perdidos a cada reinício da aplicação, e o H2 Console só enxergava os dados enquanto o processo Java que os gravou estivesse rodando. Isso costuma se manifestar como "não consigo acessar o banco de dados".

Agora o banco H2 é **persistido em arquivo** (`./data/funcionariosdb.mv.db`), então os dados sobrevivem a reinícios. No Docker, esse diretório é mantido num volume nomeado (`dixi-data`) para não se perder ao recriar o container.

- Console do H2: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:file:./data/funcionariosdb` (ajuste o caminho conforme onde você rodou o backend)
- Usuário: `sa` — sem senha
- O H2 Console é uma rota pública (não exige login), mas só funciona localmente/na rede em que o backend está rodando.

## Rodando localmente

### Backend
```
cd backend
mvn spring-boot:run
```
API disponível em `http://localhost:8080/api`.

### Frontend
```
cd frontend
npm install
npm run dev
```
Aplicação disponível em `http://localhost:5173`. Você será redirecionado para a tela de login.

### Com Docker
```
docker-compose up --build
```

## Modelo de dados

- **Usuario**: username, senha (com hash bcrypt), perfil (ADMIN ou PADRAO)
- **Funcionario**: nome, cpf
- **Cargo**: codigo, descricao
- **Departamento**: codigo, descricao
- **Vinculo**: liga um Funcionario a uma Empresa, com matricula, cargo e departamento

## Observação

Os testes automatizados (JUnit em `backend/src/test` e Cypress em `cypress/e2e`) ainda estão como esqueletos vazios e precisam ser implementados.
