# SortFilesInRootDir

## Программа, сортирующая все файлы в корневой папке и всех ее подпапках.

## Задача формулировалась так:
### Необходимо выявить все зависимости между файлами, построить сортированный список, для которого выполняется условие: если файл А, зависит от файла В, то файл А находится ниже файла В в списке.

## Ограничение на входные данные:
* Все файлы должны быть .txt формата. При этом допустимы скрытые .DS_Store файлы, генерируемые mac OS в папках.
* В файлах не должно быть опечаток в инстуркциях require. Пример правильно написанного файла:
> Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse id enim euismod erat elementum cursus. 
> In hac habitasse platea dictumst. Etiam vitae tortor ipsum. Morbi massa augue, lacinia sed nisl id, congue eleifend 
> lorem.
> 
> require ‘Folder 2/File 2-1’
> 
> Praesent feugiat egestas sem, id luctus lectus dignissim ac. Donec elementum rhoncus quam, vitae viverra massa 
> euismod a. Morbi dictum sapien sed porta tristique. Donec varius convallis quam in fringilla.

### Решение основано на топологической сортировки массива. Все файлы были представлены вершинами графа, построена матрица смежности, основанная на зависимостях файлов. После сортировки данного графа вершины были соотнесены с файлами и был выведен необходимый список в нужном порядке.
