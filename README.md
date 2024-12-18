# **Documentação do Projeto VPN com Spring Boot e Automação de Configuração**

---

## **1. Visão Geral**

O **VPN Control Panel** é um projeto desenvolvido com **Spring Boot** que oferece uma solução robusta para configurar, gerenciar e operar servidores VPN utilizando **OpenVPN**. Além disso, inclui um script de automação em Python que facilita a configuração inicial do servidor OpenVPN e a execução do backend.

**Funcionalidades principais:**
- Interface web intuitiva para iniciar ou parar conexões VPN.
- API REST para integração e automação.
- Automação para configurar OpenVPN, gerar certificados e iniciar o serviço.
- Gerenciamento seguro de comandos no servidor.

---

## **2. Tecnologias Utilizadas**

- **Spring Boot**: Framework principal para APIs REST e lógica de gerenciamento.
- **Spring Web**: Para construção de endpoints REST.
- **Spring Security**: Para autenticação e proteção de rotas sensíveis.
- **Spring Data JPA**: Persistência de dados com banco de dados.
- **Thymeleaf**: Renderização de páginas HTML.
- **OpenVPN**: Configuração e gerenciamento do servidor VPN.
- **Easy-RSA**: Geração de certificados de segurança.
- **Java 17**: Linguagem de programação usada no backend.
- **Maven**: Gerenciador de dependências.
- **Linux**: Sistema operacional base recomendado.
- **Python 3**: Para automação da configuração inicial do servidor.

---

## **3. Estrutura do Projeto**

### **Estrutura do Backend Spring Boot**

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
│   │   │   │   └── Region.java                  # Modelo de dados das regiões
│   │   │   ├── repository/
│   │   │   │   └── RegionRepository.java        # Repositório de acesso a dados
│   │   │   └── service/
│   │   │       ├── CommandService.java          # Lógica para execução de comandos
│   │   │       └── RegionService.java           # Lógica de negócios de regiões
│   │   ├── resources/
│   │   │   ├── templates/
│   │   │   │   └── index.html                   # Página principal da interface web
│   │   │   ├── static/css/
│   │   │   │   └── styles.css                   # Arquivo de estilização
│   │   │   ├── static/js/
│   │   │   │   └── app.js                       # Lógica do frontend
│   │   │   └── application.properties           # Configuração do Spring Boot
├── pom.xml                                       # Dependências do Maven
└── mvnw, mvnw.cmd                                # Wrapper Maven
```

### **Estrutura do Script de Automação**

O script **`setup_vpn.py`** executa tarefas como instalação de dependências, configuração do OpenVPN, criação de certificados e execução do backend Spring Boot.

---

## **4. Configuração do Ambiente**

### **4.1 Pré-requisitos**

- **Sistema Operacional**: Linux (Ubuntu 20.04 ou superior).
- **Java 17**: OpenJDK 17 instalado.
- **Maven**: Instalado ou usando o Maven Wrapper (`mvnw`).
- **OpenVPN** e **Easy-RSA**: Instalados no servidor.
- **Python 3**: Para executar o script de automação.

### **4.2 Configuração do Banco de Dados**

O banco de dados pode ser configurado no arquivo **`application.properties`**:

#### **H2 (Banco em memória - Testes)**:
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
git clone https://github.com/devhadis/vpn-control.git
cd vpn-control
```

### **5.2 Compilando o Projeto**
```bash
./mvnw clean install
```

### **5.3 Iniciando o Servidor**
```bash
./mvnw spring-boot:run
```

Acesse o sistema em:
```
http://localhost:8080
```

---

## **6. Uso da Aplicação**

### **6.1 Interface Web**

1. Acesse:  
   **http://localhost:8080**

2. Escolha a região no dropdown.

3. Clique em **"Connect VPN"** para iniciar a VPN.

4. Para encerrar, clique em **"Stop VPN"**.

5. A saída dos comandos será exibida na interface.

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

#### **3. Parar VPN**
- **Método**: `POST`
- **URL**: `/vpn/stop`

**Exemplo**:
```bash
curl -X POST http://localhost:8080/vpn/stop
```

---

## **7. Automação de Configuração**

O código abaixo realiza a configuração completa do servidor VPN, geração de certificados e inicialização do backend.

### **Código do Script `setup_vpn.py`**

```python
import os
import subprocess
import requests

# Funções e configuração
def run_command(command, capture_output=False):
    try:
        if capture_output:
            result = subprocess.run(command, shell=True, check=True, text=True, stdout=subprocess.PIPE)
            return result.stdout.strip()
        else:
            subprocess.run(command, shell=True, check=True)
    except subprocess.CalledProcessError as e:
        print(f"Erro ao executar comando: {command}\n{e}")
        exit(1)

def get_server_ip():
    try:
        response = requests.get("https://api.ipify.org")
        response.raise_for_status()
        return response.text
    except requests.RequestException as e:
        print(f"Erro ao obter o IP público do servidor: {e}")
        exit(1)

OPENVPN_DIR = "/etc/openvpn"
EASYRSA_DIR = os.path.expanduser("~/openvpn-ca")
SPRING_PROJECT_DIR = os.path.expanduser("~/vpn-spring-project")
GITHUB_REPO_URL = "https://github.com/devhadis/vpn-spring-project.git"
CLIENT_NAME = "client1"

# Configuração do OpenVPN e Spring Boot
print("Atualizando sistema e instalando dependências...")
run_command("sudo apt update && sudo apt install -y openvpn easy-rsa ufw openjdk-17-jdk git")
print("Gerando certificados e configurando OpenVPN...")
run_command(f"mkdir -p {EASYRSA_DIR}")
run_command(f"cp -r /usr/share/easy-rsa/* {EASYRSA_DIR}")
os.chdir(EASYRSA_DIR)
run_command("./easyrsa init-pki")
run_command("./easyrsa build-ca nopass")
run_command("./easyrsa gen-req server nopass")
run_command("./easyrsa sign-req server server")
run_command("./easyrsa gen-dh")
server_ip = get_server_ip()
print(f"Servidor configurado com IP público: {server_ip}")

# Rodando o Spring Boot
print("Baixando e executando o projeto Spring Boot...")
if not os.path.exists(SPRING_PROJECT_DIR):
    run_command(f"git clone {GITHUB_REPO_URL} {SPRING_PROJECT_DIR}")
os.chdir(SPRING_PROJECT_DIR)
run_command("./mvnw spring-boot:run")
```

---

## **8. Conclusão**

Este projeto integra **Spring Boot** e **OpenVPN** com automação para facilitar a configuração e gestão de VPN. Ele é ideal para administradores e desenvolvedores que desejam uma solução rápida e eficiente.

- **Autor**: Hadis  
- **GitHub**: [https://github.com/devhadis](https://github.com/devhadis)  
- **Data de Criação**: Julho de 2024 🚀
