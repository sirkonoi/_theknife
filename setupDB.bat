@echo off
echo ===================================================
echo       THE KNIFE - CONFIGURAZIONE DATABASE
echo ===================================================
echo.
set /p PGUSER="Inserisci l'username di Postgres [Default: postgres]: "
if "%PGUSER%"=="" set PGUSER=postgres

set /p PGPASSWORD="Inserisci la password del tuo account Postgres: "

set /p PGHOST="Inserisci l'host [Default: localhost]: "
if "%PGHOST%"=="" set PGHOST=localhost

set /p PGPORT="Inserisci la porta [Default: 5432]: "
if "%PGPORT%"=="" set PGPORT=5432

echo.
echo ---------------------------------------------------
echo [The Knife] Creazione database in corso...
echo ---------------------------------------------------

psql -U %PGUSER% -h %PGHOST% -p %PGPORT% -d postgres -c "CREATE DATABASE theknife;"

echo.
echo ---------------------------------------------------
echo [The Knife] Inizializzazione schema e dati...
echo ---------------------------------------------------

psql -U %PGUSER% -h %PGHOST% -p %PGPORT% -d theknife -f init.sql

echo.
echo ===================================================
echo  [OK] Database pronto! Benvenuto su The Knife!
echo ===================================================
echo.
pause