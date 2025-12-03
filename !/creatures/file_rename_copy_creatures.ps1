# ==== НАСТРОЙ ПУТИ ====
$Source = "D:\Homm3Reference\!\creatures\All_Creatures_HotA"
$Target = "D:\Homm3Reference\!\creatures\All_Creatures_HotA\out"

# 1. Создаем папку назначения
New-Item -ItemType Directory -Path $Target -Force | Out-Null

# 2. Получаем список файлов (исправлена ошибка с Include)
# Используем Where-Object для фильтрации расширений — это надежнее
$files = Get-ChildItem -Path $Source -File | Where-Object { $_.Extension -match '\.(png|jpg|jpeg|bmp|gif)$' }

Write-Host "Найдено файлов: $($files.Count)" -ForegroundColor Cyan

foreach ($file in $files) {
    # Получаем имя файла без расширения
    $baseName = [IO.Path]::GetFileNameWithoutExtension($file.Name)
    $extension = $file.Extension

    # ==== ЛОГИКА ПЕРЕИМЕНОВАНИЯ ====
    # 1. ToLower(): Переводим в нижний регистр (убираем "большие буквы")
    # 2. Replace: Меняем пробелы на нижнее подчеркивание (стандарт для ассетов)
    # Если нужно вообще удалить пробелы (creatureangel.png), замените '_' на ''
    $newBaseName = $baseName.ToLower() -replace ' ', '_'
    
    # Собираем новое имя
    $newName = "$newBaseName$extension"
    
    # Полный путь назначения
    $destPath = Join-Path $Target $newName

    # Копируем
    Copy-Item -Path $file.FullName -Destination $destPath -Force
}

Write-Host "Готово! Файлы скопированы в: $Target" -ForegroundColor Green