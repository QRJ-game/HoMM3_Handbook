# ==== НАСТРОЙ ПУТИ ====
$Source = "D:\Homm3Reference\!\fizmig\town_images"
$OutputFile = "D:\Homm3Reference\!\town_images.txt"

# Получаем список файлов и записываем только их имена
# -Name возвращает только имена файлов (строки), что быстрее, чем брать объекты
Get-ChildItem -Path $Source -File -Name | Set-Content -Path $OutputFile -Encoding UTF8

Write-Host "Готово! Список сохранен в: $OutputFile" -ForegroundColor Green