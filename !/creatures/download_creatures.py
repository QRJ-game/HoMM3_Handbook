import requests
import os
import re

# Настройки
BASE_URL = "https://homm.fandom.com/api.php"
CATEGORY = "Category:Creature_pictures_(HotA)" # Категория из вашей ссылки
OUTPUT_DIR = "All_Creatures_HotA"

# Создаем папку для сохранения
if not os.path.exists(OUTPUT_DIR):
    os.makedirs(OUTPUT_DIR)

def sanitize_filename(filename):
    """Убирает недопустимые символы из имени файла"""
    return re.sub(r'[\\/*?:"<>|]', "", filename)

def get_image_urls(category_name):
    """Получает список URL всех изображений в категории через API"""
    image_list = []
    params = {
        "action": "query",
        "generator": "categorymembers",
        "gcmtitle": category_name,
        "gcmtype": "file",  # Ищем только файлы
        "gcmlimit": "500",  # Максимум за один запрос
        "prop": "imageinfo",
        "iiprop": "url|mime",
        "format": "json"
    }

    print(f"Сканирую категорию {category_name}...")
    
    while True:
        response = requests.get(BASE_URL, params=params)
        data = response.json()

        if "query" not in data:
            break

        pages = data["query"]["pages"]
        for page_id in pages:
            page = pages[page_id]
            if "imageinfo" in page:
                image_info = page["imageinfo"][0]
                img_url = image_info["url"]
                # Имя файла из заголовка страницы (убираем префикс File:)
                title = page["title"].replace("File:", "").replace("Fail:", "") 
                image_list.append((title, img_url))

        # Проверка на следующую страницу результатов
        if "continue" in data:
            params.update(data["continue"])
        else:
            break
            
    return image_list

def download_file(filename, url):
    """Скачивает файл по ссылке"""
    clean_name = sanitize_filename(filename)
    path = os.path.join(OUTPUT_DIR, clean_name)
    
    if os.path.exists(path):
        print(f"Уже существует: {clean_name}")
        return

    try:
        r = requests.get(url, stream=True)
        if r.status_code == 200:
            with open(path, 'wb') as f:
                for chunk in r.iter_content(1024):
                    f.write(chunk)
            print(f"Скачано: {clean_name}")
        else:
            print(f"Ошибка {r.status_code}: {clean_name}")
    except Exception as e:
        print(f"Сбой при скачивании {clean_name}: {e}")

def main():
    images = get_image_urls(CATEGORY)
    print(f"Найдено изображений: {len(images)}")
    
    for i, (name, url) in enumerate(images):
        print(f"[{i+1}/{len(images)}] ", end="")
        download_file(name, url)

    print("\nГотово! Все файлы сохранены в папку:", OUTPUT_DIR)

if __name__ == "__main__":
    main()