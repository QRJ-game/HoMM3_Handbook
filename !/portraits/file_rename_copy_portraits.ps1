# ==== НАСТРОЙ ПУТИ ====
$Source = "D:\Homm3Reference\assets\portraits\in"          # папка, где сейчас лежат все картинки
$Target = "D:\Homm3Reference\assets\portraits\out"     # куда копировать отфильтрованные

# создать папку назначения, если её нет
New-Item -ItemType Directory -Path $Target -Force | Out-Null

# можно расширить список разрешённых расширений при необходимости
$files = Get-ChildItem -Path $Source -File -Include *.png,*.jpg,*.jpeg,*.bmp,*.gif

# группируем по "основе" имени (без _small и _(HotA))
$groups = $files | Group-Object -Property {
    $name = [IO.Path]::GetFileNameWithoutExtension($_.Name)
    # убираем служебные хвосты
    $name -replace '_small$','' -replace '_\(HotA\)$',''
}

foreach ($g in $groups) {

    # кандидаты: без _small в конце
    $candidates = $g.Group | Where-Object { $_.BaseName -notmatch '_small$' }
    if (-not $candidates) { continue }

    # сначала пробуем найти вариант с HotA
    $selected = $candidates | Where-Object { $_.BaseName -match 'HotA' } | Select-Object -First 1

    # если HotA нет — берём первый обычный
    if (-not $selected) {
        $selected = $candidates | Select-Object -First 1
    }

    # копируем выбранный файл
    Copy-Item -Path $selected.FullName -Destination (Join-Path $Target $selected.Name) -Force
}
