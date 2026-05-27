
### 👤 Pessoas
Endpoints para gerenciamento de pessoas.


| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `GET` | `/api/pessoas` | Listar todas as pessoas |
| `POST` | `/api/pessoas` | Cadastrar nova Pessoa |
| `DELETE` | `/api/pessoas/{id}` | Deletar Pessoa |
| `PATCH` | `/api/pessoas/{id}` | Atualizar Pessoa |
| `GET` | `/api/pessoas/{id}/minha-idade` | Verificar idade da Pessoa |


### 📍 Endereços
Endpoints para gerenciamento de endereços das pessoas.


| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `PATCH` | `/api/pessoas/{pessoaId}/enderecos/{enderecoId}` | Atualizar endereço |
| `PATCH` | `/api/pessoas/{pessoaId}/enderecos/mudar-principal/{enderecoId}` | Mudar endereço principal |
| `GET` | `/api/pessoas/{pessoaId}/enderecos` | Listar endereços da pessoa |
| `POST` | `/api/pessoas/{pessoaId}/enderecos` | Adicionar um endereço |
