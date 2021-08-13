# Project Sustainable Foraging 
## Features Update & Additions

## Missing Features
#### 1) View Foragers

- MainMenuOption Enum
    - Add an enum VIEW_FORAGERS

- Controller Class
  - Add relevant option in switch block of runAppLoop method
  - Create method viewForagers()

- View Class
   - Create methods chooseState() and displayForagers(List\<Forager> foragers)
    
- States Enum
   - Create a new Enum

(findByState methods already exist in ForagerService and ForagerFileRepository classes)

#### 2) Validation 
Requirement for adding a Forage is: combination of date, item & Forager cannot be duplicate.

- ForageService Class
   - Create method checkDuplicate(Forage forage, Result\<Forage> result) and call it from the validate method
- ForageServiceTest Class
   - Create shouldNotAddDuplicateForage()
- Forager Class
  - Create equals method (using firstName, lastName, state fields)  

### Incomplete Features
#### 1) Add a Forager 
- Controller Class
   - Create method addForager()

- View Class
   - Create method makeForager() (and chooseState() -> already added for fixing View Foragers feature)

- ForagerService Class
   - Create methods validate(Forager forager), validateNulls(Forager forager), checkDuplicate(Forager forager, Result\<Forager> result) and addForager(Forager forager)

- ForagerServiceTest Class (create this class)
   - Create methods shouldAddForager() and shouldNotAddDuplicateForager()
  
- ForagerFileRepository Class
   - Create methods add(Forager forager), serialize(Forager forager) and writeAll(List\<Forager> foragers) \
     (add method creates GUID before writing forager to file)

- ForagerFileRepositoryTest Class 
   - add two files in data foragers-seed.txt and foragers-test.txt
   - Create method shouldAddForager()
  
#### 2) Add reports for a day

- Controller Class
   - Create methods dayKgReport() and dayValueByCategoryReport()

- View Class
   - Create methods printDayKgReport(Map\<Item, Double> map)  and printDayValueByCategoryReport(Map\<Category, BigDecimal> map)

- ItemService Class
   - Create method findById(int id)
  
- ForageService Class
    - Create methods getDayKgReport(LocalDate date) and getDayValueByCategoryReport(LocalDate date)

- ForageServiceTest Class
   - Create methods shouldCreateCorrectKgReport() and shouldCreateCorrectValueByCategoryReport()  \
     (added foragers, forages, items in respective double repositories to check these methods)
  

**********************************************************
To prevent commas from being added to data, each String data type uses "clean" & "restore" methods in its corresponding repository. \
name in Item class, first name & last name in Forager class



