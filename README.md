# Fin Capital Backend v2 Full

This backend is aligned to the final `FinCapitalDB` schema and removes the old `branch_location` mismatch.

## SQL Server used

- Server: `KAVI\TEW_SQLEXPRESS`
- Database: `FinCapitalDB`
- Username: `sa`

The SQL password is NOT saved in this ZIP.

## Run it

Open PowerShell inside this backend folder.

### 1. Set password for this terminal only

```powershell
$env:DB_PASSWORD="YOUR_SQL_PASSWORD"
```

### 2. Build

```powershell
mvn clean package -DskipTests
```

### 3. Start

```powershell
mvn spring-boot:run
```

### 4. Test

Open:

```text
http://localhost:8080/api/health
```

Expected:

```json
{"status":"UP","app":"Fin Capital Backend v2"}
```

## Important configuration

`application.yml` uses:

```yaml
ddl-auto: validate
```

Hibernate will validate your manually created SQL Server tables but will not alter them.

## Implemented APIs

- `GET /api/health`
- `POST /api/companies`
- `GET /api/companies`
- `POST /api/branches`
- `GET /api/branches?companyId=1`
- `POST /api/agents/register`
- `POST /api/agents/{id}/verify-mobile`
- `POST /api/agents/{id}/approve`
- `POST /api/agents/{id}/reject`
- `GET /api/agents?companyId=1`
- `POST /api/customers`
- `GET /api/customers?companyId=1&branchId=1`
- `GET /api/customers/{id}`
- `POST /api/loans`
- `GET /api/loans?companyId=1&branchId=1`
- `POST /api/payments`
- `GET /api/payments?companyId=1&branchId=1`
- `POST /api/expenses`
- `GET /api/expenses?companyId=1&branchId=1`
- `DELETE /api/expenses/{id}`
- `GET /api/dashboard/summary?companyId=1&branchId=1`
- `GET /api/reports/overall?companyId=1&branchId=1&from=2026-08-01&to=2026-08-31`

## Business rules included

### IDs

For company `Sangam Fin Capital`:

- Company code: `SFC`
- Customer: `SFC-0001`
- Loan: `SFCLN-00001`
- Agent: `SFCEMP-0001`
- Payment: `SFCPAY-000001`
- Expense: `SFCEXP-000001`

### Agent

- Profile photo is mandatory.
- Registration starts as `PENDING_OTP`.
- Verify mobile → `PENDING_APPROVAL`.
- MD approves directly, with no second owner OTP.
- Employee ID is generated only on approval.

### Loan

EMI:
- Daily = 100 days
- Weekly = 10 weeks
- Monthly = 10 months

IO:
- Duration is manual.
- Interest collection amount = interest amount per cycle.

Due dates:
- Daily first due = next day.
- Weekly first due = +7 days and stays on same weekday.
- Monthly first due = same date next month.

### Transactions

- Collection = IN
- Fine = IN
- New loan amount given = OUT
- Expense = OUT

## Next step

Once `/api/health` works, connect the React frontend to these endpoints.
