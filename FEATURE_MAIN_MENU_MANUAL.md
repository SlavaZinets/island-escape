# Feature: Game Manual from Main Menu

## Branch
`feature/main-menu-manual` (22 commits from main)

## Overview
Добавлена главная меню с доступом к игровому мануалу. При старте игры игрок видит экран меню с тремя опциями: Start Game, Manual, Exit.

## What's Implemented

### 1. Main Menu Screen
- Экран с выбором трёх опций: `Start Game`, `Manual`, `Exit`
- Навигация стрелками вверх/вниз и Enter
- Поддержка клика мышкой по пункту
- Циклическое листание (бесконечное прокручивание)
- Красивый UI с подсветкой выбранного пункта

### 2. Manual Screen
- Отдельный экран с игровым руководством
- Содержит информацию о целях, управлении и советах
- Выход: ESC для возврата в главное меню

### 3. Game State Management
- Добавлены новые состояния: `MAIN_MENU` и `MANUAL`
- Корректная логика переходов между состояниями
- При ESC из Manual — возврат в главное меню

### 4. Input Handling
- Расширена `GameKeyHandler` для поддержки меню (Up/Down/Enter)
- Новый класс `MainMenuMouseHandler` для клика мышкой
- Безопасная обработка null-paneli в тестах

## Files Changed
- `src/java/com/islandescape/window/GameState.java` — добавлены состояния
- `src/java/com/islandescape/ui/MainMenuScreen.java` — UI главного меню
- `src/java/com/islandescape/ui/ManualScreen.java` — UI мануала
- `src/java/com/islandescape/window/GamePanel.java` — интеграция и переходы
- `src/java/com/islandescape/input/GameKeyHandler.java` — обработка клавиш
- `src/java/com/islandescape/input/MainMenuMouseHandler.java` — обработка мыши
- `src/test/java/com/islandescape/input/GameKeyHandlerTest.java` — тесты

## How to Test

### Setup
1. Переключиться на ветку: `git checkout feature/main-menu-manual`
2. Собрать: `javac -encoding UTF-8 -d build/classes $(find src/java -name "*.java")`
3. Запустить: `java -cp build/classes com.islandescape.Main`

### Test Scenarios

#### Scenario 1: Main Menu Navigation (Keyboard)
1. При старте должен открыться экран главного меню
2. Нажать Up/Down — должна меняться подсветка пункта
3. Enter — активирует выбранный пункт

#### Scenario 2: Main Menu Navigation (Mouse)
1. Нажать на любой пункт мышкой — должен сразу активироваться
2. Клик вне пунктов — не должно ничего происходить

#### Scenario 3: Manual Screen
1. Выбрать "Manual" из меню (клавиатура или мышка)
2. Должен открыться экран с информацией
3. Нажать ESC — должен вернуться в главное меню

#### Scenario 4: Start Game
1. Выбрать "Start Game"
2. Должен начаться обычный игровой режим

#### Scenario 5: Exit
1. Выбрать "Exit"
2. Приложение должно корректно закрыться

#### Scenario 6: Cyclical Navigation
1. На последнем пункте нажать Down — должен выбраться первый пункт
2. На первом пункте нажать Up — должен выбраться последний пункт
3. Это должно работать бесконечно без ошибок

## Testing Results
- ✅ Compilation: успешна, без ошибок
- ✅ Game Startup: запускается без ошибок
- ✅ Key Handler Tests: 25/25 passed
- ✅ Cyclical Navigation: безопасна
- ✅ Menu Selection: работает с клавиатурой и мышкой

## Known Issues
- Полный прогон всех старых тестов сейчас блокируется двумя существующими проблемами вне этой задачи (BoatWreackTest, CraftingTableTest)

## Notes
- Изменения максимально изолированы от существующего кода
- Новая функциональность не ломает текущий gameplay
- Можно безопасно мержить в main
