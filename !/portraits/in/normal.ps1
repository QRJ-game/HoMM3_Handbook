# --- НАСТРОЙКИ ---
$sourceDir = "D:\Homm3Reference\assets\portraits\in"   # Где лежат исходники
$destDir   = "D:\Homm3Reference\assets\portraits\out" # Куда класть готовые
$listName  = "files_list.txt"                        # Имя файла со списком
# -----------------

# 1. Создаем папку назначения
if (!(Test-Path $destDir)) { New-Item -Type Directory -Path $destDir | Out-Null }

# 2. Получаем файлы (без _small)
$files = Get-ChildItem -Path $sourceDir -Filter "*.png" | 
         Where-Object { $_.BaseName -notlike "*_small" }

# 3. Группируем. 
# Сначала убираем "_(HotA)" для поиска пар.
$groupedFiles = $files | Group-Object { 
    $_.BaseName -replace "_\(HotA\)$", "" 
}

Write-Host "Обработка и переименование..." -ForegroundColor Yellow

foreach ($group in $groupedFiles) {
    # --- ШАГ А: ВЫБОР ФАЙЛА ---
    # Ищем версию с HotA
    $hotaVersion = $group.Group | Where-Object { $_.BaseName -like "*_(HotA)" }
    
    if ($hotaVersion) { 
        $sourceFile = $hotaVersion | Select-Object -First 1 
    } else { 
        $sourceFile = $group.Group[0] 
    }

    # --- ШАГ Б: ФОРМИРОВАНИЕ ИМЕНИ ---
    # Берем имя группы (это имя без суффикса HotA)
    $rawName = $group.Name 

    # 1. Переводим в нижний регистр (Lower case)
    $cleanName = $rawName.ToLower()

    # 2. Заменяем скобки ( ) и пробелы на _
    $cleanName = $cleanName -replace "[\(\)\s]", "_"

    # 3. Убираем двойные подчеркивания (если возникли) и лишние по краям
    $cleanName = $cleanName -replace "_+", "_"  # __ -> _
    $cleanName = $cleanName -replace "_$", ""   # убрать _ в конце
    $cleanName = $cleanName -replace "^_", ""   # убрать _ в начале

    # Добавляем расширение
    $finalName = "$cleanName.png"
    
    # --- ШАГ В: КОПИРОВАНИЕ ---
    $destinationPath = Join-Path -Path $destDir -ChildPath $finalName
    Copy-Item -Path $sourceFile.FullName -Destination $destinationPath -Force
}

# --- ШАГ Г: СОЗДАНИЕ СПИСКА ---
Write-Host "Генерация списка ($listName)..." -ForegroundColor Yellow

$listPath = Join-Path -Path $destDir -ChildPath $listName

# Получаем список уже обработанных файлов из новой папки
Get-ChildItem -Path $destDir -Filter "*.png" | 
    Select-Object -ExpandProperty Name | 
    Out-File -FilePath $listPath -Encoding UTF8

Write-Host "Готово! Обработано групп: $($groupedFiles.Count)" -ForegroundColor Green