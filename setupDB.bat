@echo off
echo ===================================================
echo       THE KNIFE - CONFIGURAZIONE DATABASE
echo ===================================================
echo.

set /p PGUSER="Inserisci l'username di Postgres [Default: postgres]: "
if "%PGUSER%"=="" set PGUSER=postgres

set /p PGPASSWORD="Inserisci la password del tuo Postgres: "

set /p PGHOST="Inserisci l'host [Default: localhost]: "
if "%PGHOST%"=="" set PGHOST=localhost

set /p PGPORT="Inserisci la porta [Default: 5432]: "
if "%PGPORT%"=="" set PGPORT=5432

set PGDATABASE=postgres

echo.
echo ---------------------------------------------------
echo [The Knife] Inizializzazione del Database in corso...
echo ---------------------------------------------------

psql -f init.sql

echo.
echo ===================================================
echo  [OK] Database pronto! Benvenuto su The Knife!
echo ===================================================
echo.
pause