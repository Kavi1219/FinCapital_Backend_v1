# Fin Capital Backend - Frontend Match Update

Frontend source was inspected only and was not modified.

Backend/database alignment added for the current React data model:
- customer father name
- Jamin father name
- multiple-document database ownership support (CUSTOMER/JAMIN/AGENT)
- loan interestTakenUpfront
- amountGiven matching frontend rule
- totalRepayment matching frontend rule
- user-selected duration for Daily/Weekly/Monthly
- durationUnit
- collectedAmount, principalPending, pendingDue, fineDue, finePaidTotal
- precloseAmount, preclosedAt, closedAt
- Borrow/Loan Given as OUT transaction when a loan is created
- payment date support and collector IDs
- loan summary updates after payments
- preclose endpoint and PRECLOSE transaction

Important: the supplied React frontend still reads/writes localStorage and IndexedDB. It does not call these backend APIs yet. Per instruction, no frontend file was changed. The backend/database now model the same information, but data will remain local to the browser until the frontend is explicitly connected to the API in a later step.
