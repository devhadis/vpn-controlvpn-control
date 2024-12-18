# **Documentação do Projeto VPN com Spring Boot**

## **1. Visão Geral**

O projeto **VPN Control Panel** é uma aplicação Spring Boot que permite **gerenciar servidores VPN** com facilidade. Através de uma interface web simples e endpoints de API REST, o projeto permite:

- Escolher regiões para iniciar ou parar a VPN.
- Gerenciar configurações de VPN usando OpenVPN.
- Facilitar a execução de comandos pré-definidos no servidor.

---

## **2. Tecnologias Utilizadas**

- **Spring Boot**: Framework principal para criação de APIs e gerenciamento.
- **Spring Web**: Para construção de endpoints REST.
- **Spring Security**: Para proteger rotas sensíveis com autenticação.
- **Spring Data JPA**: Para acesso ao banco de dados (H2 ou MySQL).
- **Thymeleaf**: Para renderização de páginas HTML.
- **OpenVPN**: Ferramenta para criação e gerenciamento de VPN.
- **Easy-RSA**: Gerenciamento de certificados e chaves.
- **Java 17**: Versão do JDK.
- **Maven**: Gerenciador de dependências.
- **Linux**: Sistema operacional base para execução do servidor OpenVPN.

---

## **3. Estrutura do Projeto**

```plaintext
vpn-control/
├── src/
│   ├── main/
│   │   ├── java/com/vpnmanager/
│   │   │   ├── VpnControlApplication.java       # Classe principal
│   │   │   ├── config/
│   │   │   │   └── SecurityConfig.java          # Configurações de segurança
│   │   │   ├── controller/
│   │   │   │   ├── RegionController.java        # Gerencia rotas das regiões
│   │   │   │   └── VpnController.java           # Gerencia as VPNs
│   │   │   ├── model/
│   │   │   │   └── Region.java                  # Modelo de dados da região
│   │   │   ├── repository/
│   │   │   │   └── RegionRepository.java        # Repositório JPA
│   │   │   └── service/
│   │   │       ├── CommandService.java          # Executa comandos no sistema
│   │   │       └── RegionService.java           # Lógica de negócio
│   │   ├── resources/
│   │   │   ├── templates/
│   │   │   │   └── index.html                   # Interface web (Thymeleaf)
│   │   │   ├── static/css/
│   │   │   │   └── styles.css                   # Estilização da interface
│   │   │   ├── static/js/
│   │   │   │   └── app.js                       # Lógica do frontend
│   │   │   └── application.properties           # Configuração do Spring Boot
├── pom.xml                                       # Dependências do Maven
└── mvnw, mvnw.cmd                                # Wrapper Maven
```

---

## **4. Configuração do Ambiente**

### **4.1 Pré-Requisitos**

- **Linux**: Recomendado (Ubuntu 20.04 ou superior).
- **Java 17**: Instalar OpenJDK 17.
- **Maven**: Instalar Maven (ou usar o Maven Wrapper fornecido).
- **OpenVPN** e **Easy-RSA**: Configurados no servidor.

### **4.2 Configuração do Banco de Dados**

No arquivo **`application.properties`**, você pode configurar o banco:

#### **H2 (Em memória para testes)**:
```properties
spring.datasource.url=jdbc:h2:mem:vpnmanager
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
spring.h2.console.enabled=true
```

#### **MySQL (Produção)**:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/vpn_manager
spring.datasource.username=root
spring.datasource.password=senha
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

---

## **5. Como Rodar o Projeto**

### **5.1 Clonando o Repositório**

```bash
git clone https://github.com/seu-usuario/vpn-control.git
cd vpn-control
```

### **5.2 Compilando o Projeto**

Execute o Maven Wrapper para compilar o projeto:

```bash
./mvnw clean install
```

### **5.3 Rodando o Projeto**

Inicie o servidor Spring Boot:

```bash
./mvnw spring-boot:run
```

Acesse a aplicação no navegador em:

```
http://localhost:8080
```

---

## **6. Uso da Aplicação**

### **6.1 Interface Web**

1. Acesse a página principal:  
   ```plaintext
   http://localhost:8080
   ```

2. Escolha uma **região** no dropdown da interface.

3. Clique no botão **"Connect VPN"** para iniciar a VPN.

4. Para encerrar a VPN, clique em **"Stop VPN"**.

5. A saída do comando aparecerá na tela.

---

### **6.2 API REST**

#### **1. Listar Regiões Disponíveis**

- **Método**: `GET`
- **URL**: `/regions`

**Exemplo de Resposta**:
```json
[
  { "id": 1, "name": "Brasil - São Paulo", "command": "openvpn@br" },
  { "id": 2, "name": "EUA - Nova York", "command": "openvpn@us-east" }
]
```

#### **2. Iniciar VPN**

- **Método**: `POST`
- **URL**: `/vpn/start/{regionId}`

**Exemplo**:
```bash
curl -X POST http://localhost:8080/vpn/start/1
```

**Resposta**:
```plaintext
Comando executado com sucesso.
```

#### **3. Parar VPN**

- **Método**: `POST`
- **URL**: `/vpn/stop`

**Exemplo**:
```bash
curl -X POST http://localhost:8080/vpn/stop
```

**Resposta**:
```plaintext
VPN stopped successfully.
```

---

## **7. Segurança**

O projeto usa **Spring Security** para proteger endpoints sensíveis. A autenticação básica é configurada com o usuário:

- **Usuário**: `admin`
- **Senha**: `password123`

Você pode ajustar essas configurações no **`SecurityConfig.java`**.

---

## **8. Funcionamento com OpenVPN**

O backend Spring Boot chama comandos do sistema usando o **`CommandService`** para:

1. **Iniciar a VPN**:  
   Exemplo:  
   ```bash
   sudo systemctl start openvpn@br
   ```

2. **Parar a VPN**:  
   Exemplo:  
   ```bash
   sudo systemctl stop openvpn@server
   ```

3. Esses comandos são **pré-validados** no servidor e protegidos contra injeção.

---

## **9. Teste da Conexão VPN**

1. **Cliente OpenVPN**:  
   Transfira o arquivo `.ovpn` gerado para o cliente:

   ```bash
   scp ~/openvpn-ca/clients/client1.ovpn user@client-machine:/path/
   ```

2. **No Cliente**:
   ```bash
   sudo openvpn --config client1.ovpn
   ```

3. **Verifique o IP Público**:
   ```bash
   curl https://api.ipify.org
   ```

Se o IP público mudar para o do servidor VPN, a configuração está correta.

---

## **10. Conclusão**

Este projeto automatiza a criação e o gerenciamento de conexões VPN utilizando **OpenVPN** e fornece uma interface gerenciada por **Spring Boot**. A interface permite ao usuário final gerenciar facilmente servidores VPN em várias regiões.

Se você tiver alguma dúvida ou problema, contribua com o projeto no GitHub!

---

**Autor**: Seu Nome  
**GitHub**: [Seu Repositório](#)  
**Data de Criação**: Julho de 2024 🚀# **Documentação do Projeto VPN com Spring Boot**

## **1. Visão Geral**

O projeto **VPN Control Panel** é uma aplicação Spring Boot que permite **gerenciar servidores VPN** com facilidade. Através de uma interface web simples e endpoints de API REST, o projeto permite:

- Escolher regiões para iniciar ou parar a VPN.
- Gerenciar configurações de VPN usando OpenVPN.
- Facilitar a execução de comandos pré-definidos no servidor.

---

## **2. Tecnologias Utilizadas**

- **Spring Boot**: Framework principal para criação de APIs e gerenciamento.
- **Spring Web**: Para construção de endpoints REST.
- **Spring Security**: Para proteger rotas sensíveis com autenticação.
- **Spring Data JPA**: Para acesso ao banco de dados (H2 ou MySQL).
- **Thymeleaf**: Para renderização de páginas HTML.
- **OpenVPN**: Ferramenta para criação e gerenciamento de VPN.
- **Easy-RSA**: Gerenciamento de certificados e chaves.
- **Java 17**: Versão do JDK.
- **Maven**: Gerenciador de dependências.
- **Linux**: Sistema operacional base para execução do servidor OpenVPN.

---

## **3. Estrutura do Projeto**

```plaintext
vpn-control/
├── src/
│   ├── main/
│   │   ├── java/com/vpnmanager/
│   │   │   ├── VpnControlApplication.java       # Classe principal
│   │   │   ├── config/
│   │   │   │   └── SecurityConfig.java          # Configurações de segurança
│   │   │   ├── controller/
│   │   │   │   ├── RegionController.java        # Gerencia rotas das regiões
│   │   │   │   └── VpnController.java           # Gerencia as VPNs
│   │   │   ├── model/
│   │   │   │   └── Region.java                  # Modelo de dados da região
│   │   │   ├── repository/
│   │   │   │   └── RegionRepository.java        # Repositório JPA
│   │   │   └── service/
│   │   │       ├── CommandService.java          # Executa comandos no sistema
│   │   │       └── RegionService.java           # Lógica de negócio
│   │   ├── resources/
│   │   │   ├── templates/
│   │   │   │   └── index.html                   # Interface web (Thymeleaf)
│   │   │   ├── static/css/
│   │   │   │   └── styles.css                   # Estilização da interface
│   │   │   ├── static/js/
│   │   │   │   └── app.js                       # Lógica do frontend
│   │   │   └── application.properties           # Configuração do Spring Boot
├── pom.xml                                       # Dependências do Maven
└── mvnw, mvnw.cmd                                # Wrapper Maven
```

---

## **4. Configuração do Ambiente**

### **4.1 Pré-Requisitos**

- **Linux**: Recomendado (Ubuntu 20.04 ou superior).
- **Java 17**: Instalar OpenJDK 17.
- **Maven**: Instalar Maven (ou usar o Maven Wrapper fornecido).
- **OpenVPN** e **Easy-RSA**: Configurados no servidor.

### **4.2 Configuração do Banco de Dados**

No arquivo **`application.properties`**, você pode configurar o banco:

#### **H2 (Em memória para testes)**:
```properties
spring.datasource.url=jdbc:h2:mem:vpnmanager
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
spring.h2.console.enabled=true
```

#### **MySQL (Produção)**:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/vpn_manager
spring.datasource.username=root
spring.datasource.password=senha
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

---

## **5. Como Rodar o Projeto**

### **5.1 Clonando o Repositório**

```bash
git clone https://github.com/seu-usuario/vpn-control.git
cd vpn-control
```

### **5.2 Compilando o Projeto**

Execute o Maven Wrapper para compilar o projeto:

```bash
./mvnw clean install
```

### **5.3 Rodando o Projeto**

Inicie o servidor Spring Boot:

```bash
./mvnw spring-boot:run
```

Acesse a aplicação no navegador em:

```
http://localhost:8080
```

---

## **6. Uso da Aplicação**

### **6.1 Interface Web**

1. Acesse a página principal:  
   ```plaintext
   http://localhost:8080
   ```

2. Escolha uma **região** no dropdown da interface.

3. Clique no botão **"Connect VPN"** para iniciar a VPN.

4. Para encerrar a VPN, clique em **"Stop VPN"**.

5. A saída do comando aparecerá na tela.

---

### **6.2 API REST**

#### **1. Listar Regiões Disponíveis**

- **Método**: `GET`
- **URL**: `/regions`

**Exemplo de Resposta**:
```json
[
  { "id": 1, "name": "Brasil - São Paulo", "command": "openvpn@br" },
  { "id": 2, "name": "EUA - Nova York", "command": "openvpn@us-east" }
]
```

#### **2. Iniciar VPN**

- **Método**: `POST`
- **URL**: `/vpn/start/{regionId}`

**Exemplo**:
```bash
curl -X POST http://localhost:8080/vpn/start/1
```

**Resposta**:
```plaintext
Comando executado com sucesso.
```

#### **3. Parar VPN**

- **Método**: `POST`
- **URL**: `/vpn/stop`

**Exemplo**:
```bash
curl -X POST http://localhost:8080/vpn/stop
```

**Resposta**:
```plaintext
VPN stopped successfully.
```

---

## **7. Segurança**

O projeto usa **Spring Security** para proteger endpoints sensíveis. A autenticação básica é configurada com o usuário:

- **Usuário**: `admin`
- **Senha**: `password123`

Você pode ajustar essas configurações no **`SecurityConfig.java`**.

---

## **8. Funcionamento com OpenVPN**

O backend Spring Boot chama comandos do sistema usando o **`CommandService`** para:

1. **Iniciar a VPN**:  
   Exemplo:  
   ```bash
   sudo systemctl start openvpn@br
   ```

2. **Parar a VPN**:  
   Exemplo:  
   ```bash
   sudo systemctl stop openvpn@server
   ```

3. Esses comandos são **pré-validados** no servidor e protegidos contra injeção.

---

## **9. Teste da Conexão VPN**

1. **Cliente OpenVPN**:  
   Transfira o arquivo `.ovpn` gerado para o cliente:

   ```bash
   scp ~/openvpn-ca/clients/client1.ovpn user@client-machine:/path/
   ```

2. **No Cliente**:
   ```bash
   sudo openvpn --config client1.ovpn
   ```

3. **Verifique o IP Público**:
   ```bash
   curl https://api.ipify.org
   ```

Se o IP público mudar para o do servidor VPN, a configuração está correta.

---

## **10. Conclusão**

Este projeto automatiza a criação e o gerenciamento de conexões VPN utilizando **OpenVPN** e fornece uma interface gerenciada por **Spring Boot**. A interface permite ao usuário final gerenciar facilmente servidores VPN em várias regiões.

Se você tiver alguma dúvida ou problema, contribua com o projeto no GitHub!

---

**Autor**: Hadis  
**GitHub**: [https://github.com/devhadis](#)  
**Data de Criação**: Julho de 2024 🚀
