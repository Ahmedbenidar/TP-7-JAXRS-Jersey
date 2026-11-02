<#
    Teste l'ensemble des endpoints JAX-RS de l'application et journalise
    les vraies requetes curl ainsi que leurs vraies reponses.

    Prerequis : l'application doit tourner sur http://localhost:8082
    Utilisation :
        powershell -ExecutionPolicy Bypass -File docs\tools\tester-api.ps1
#>

$base = "http://localhost:8082"
$payloads = Join-Path $PSScriptRoot "..\payloads"

function Titre($texte) {
    Write-Output ""
    Write-Output "### $texte ###"
}

function Appel($commentaire, $methode, $url, $entetes, $corps) {
    if ($commentaire) { Write-Output "# $commentaire" }

    $args = @("-s", "-w", "`n[HTTP %{http_code}]", "-X", $methode, $url)
    foreach ($e in $entetes) { $args += @("-H", $e) }
    if ($corps) { $args += @("-d", "@$corps") }

    $affichage = "curl.exe -s -X $methode `"$url`""
    foreach ($e in $entetes) { $affichage += " -H `"$e`"" }
    if ($corps) { $affichage += " -d `"@$corps`"" }
    Write-Output ""
    Write-Output "PS> $affichage"

    & curl.exe @args | ForEach-Object { Write-Output $_ }
}

Write-Output "=============================================================="
Write-Output " TEST DES SERVICES REST JAX-RS / JERSEY - APPLICATION BANQUE"
Write-Output " Cible : $base"
Write-Output " Date  : $(Get-Date -Format 'yyyy-MM-dd HH:mm:ss')"
Write-Output "=============================================================="

Titre "1. L'application repond-elle ?"
Appel "Verification de la disponibilite du service" "GET" "$base/banque/comptes/1" @("Accept: application/json") $null

Titre "2. READ - tous les comptes au format JSON"
Appel "En-tete Accept: application/json" "GET" "$base/banque/comptes" @("Accept: application/json") $null

Titre "3. READ - tous les comptes au format XML"
Appel "Meme endpoint, en-tete Accept: application/xml" "GET" "$base/banque/comptes" @("Accept: application/xml") $null

Titre "4. READ - un compte par identifiant (JSON)"
Appel "" "GET" "$base/banque/comptes/2" @("Accept: application/json") $null

Titre "5. READ - le meme compte au format XML"
Appel "@XmlRootElement permet la serialisation JAXB" "GET" "$base/banque/comptes/2" @("Accept: application/xml") $null

Titre "6. READ - filtrage par type de compte"
Appel "Parametre de requete ?type=EPARGNE" "GET" "$base/banque/comptes?type=EPARGNE" @("Accept: application/json") $null

Titre "7. READ - recherche par intervalle de solde"
Appel "Parametres ?soldeMin=10000&soldeMax=20000" "GET" "$base/banque/comptes?soldeMin=10000&soldeMax=20000" @("Accept: application/json") $null

Titre "8. READ - un compte par sa reference metier"
Appel "" "GET" "$base/banque/comptes/reference/BQ-2026-000102" @("Accept: application/json") $null

Titre "9. READ - indicateurs agreges"
Appel "" "GET" "$base/banque/comptes/statistiques" @("Accept: application/json") $null

Titre "10. CREATE - ajout d'un compte (JSON)"
Write-Output "# Corps de requete envoye :"
Get-Content (Join-Path $payloads "compte-nouveau.json") | ForEach-Object { Write-Output "  $_" }
Appel "" "POST" "$base/banque/comptes" @("Content-Type: application/json", "Accept: application/json") (Join-Path $payloads "compte-nouveau.json")

Titre "11. CREATE - ajout d'un compte (XML)"
Appel "Le service consomme aussi du XML (@Consumes application/xml)" "POST" "$base/banque/comptes" @("Content-Type: application/xml", "Accept: application/xml") (Join-Path $payloads "compte-nouveau.xml")

Titre "12. UPDATE - modification du compte 1"
Appel "Nouveau solde : 14000.00" "PUT" "$base/banque/comptes/1" @("Content-Type: application/json", "Accept: application/json") (Join-Path $payloads "compte-modifie.json")

Titre "13. Gestion d'erreur - compte inexistant"
Appel "L'ExceptionMapper JAX-RS transforme l'erreur en HTTP 404 JSON" "GET" "$base/banque/comptes/9999" @("Accept: application/json") $null

Titre "14. DELETE - suppression du compte cree au point 11"
$liste = & curl.exe -s -X GET "$base/banque/comptes" -H "Accept: application/json" | ConvertFrom-Json
$cible = @($liste.compte) | Where-Object { $_.numeroCompte -eq 'BQ-2026-000201' } | Select-Object -First 1
if ($cible) {
    Appel "Suppression du compte id=$($cible.id)" "DELETE" "$base/banque/comptes/$($cible.id)" @() $null
} else {
    Write-Output "# Aucun compte BQ-2026-000201 a supprimer."
}

Titre "15. VERIFICATION FINALE - etat de la table"
Appel "Indicateurs apres les operations CRUD" "GET" "$base/banque/comptes/statistiques" @("Accept: application/json") $null

Write-Output ""
Write-Output "=============================================================="
Write-Output " FIN DES TESTS"
Write-Output "=============================================================="