#
# This script prepares the repository for publishing a new open source version.
# It removes sensitive or unnecessary files, renames specific files, and ensures
# the repository is in the correct state for public release.
#

# List of specific files to delete
$files = @(
    "CHANGELOG.md",
    "Dockerfile",
    "Dockerfile-rh",
    "docker-compose.yml",
    "complex-event-processing\src\main\resources\keystore.p12",
    "complex-event-processing\src\main\resources\localhost.cer"
)

# Delete specific files
foreach ($file in $files) {
    if (Test-Path $file) {
        Remove-Item $file
        Write-Host "Deleted: $file" -ForegroundColor Green
    } else {
        Write-Host "Not found: $file" -ForegroundColor Yellow
    }
}

# Delete docker-compose-*.yml files
Get-ChildItem -Path "." -Filter "docker-compose-*.yml" | ForEach-Object {
    Remove-Item $_.FullName
    Write-Host "Deleted: $($_.Name)" -ForegroundColor Green
}

# Delete all .ps1 files under the database folder
if (Test-Path "database") {
    Get-ChildItem -Path "database" -Filter "*.ps1" -Recurse | ForEach-Object {
        Remove-Item $_.FullName
        Write-Host "Deleted: $($_.FullName)" -ForegroundColor Green
    }
} else {
    Write-Host "Warning: database folder not found" -ForegroundColor Yellow
}

# Rename Dockerfile.public to Dockerfile
if (Test-Path "Dockerfile.public") {
    Rename-Item -Path "Dockerfile.public" -NewName "Dockerfile"
    Write-Host "Renamed: Dockerfile.public to Dockerfile" -ForegroundColor Green
} else {
    Write-Host "Warning: Dockerfile.public not found" -ForegroundColor Yellow
}