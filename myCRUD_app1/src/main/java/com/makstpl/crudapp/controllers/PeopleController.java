package com.makstpl.crudapp.controllers;

import com.makstpl.crudapp.dao.PersonDAO;
import com.makstpl.crudapp.models.Person;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PersonDAO personDAO;

    @Autowired
    public PeopleController(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @GetMapping
    public String index(Model model) {
        //Получим всех людей из DAO и передадим на отображение в представление

        model.addAttribute("people", personDAO.index());
        return "people/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        //Получим одного человека по id из DAO и передадим на отображение в представление

        model.addAttribute("person", personDAO.show(id));
        return "people/show";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person) {
        return "people/new";
    }

    //Метод для создания нового человека и добавления его в базу данных
    @PostMapping
    public String create(@ModelAttribute("person") @Valid Person person,  //В этом объекте будет лежать человек с данными из формы, реализуемой в файле "new"
                         BindingResult bindingResult) { //Тут будут лежать все ошибки валидации класса Person

        if(bindingResult.hasErrors())
            return "people/new";

        personDAO.save(person);//Помещаем объект в модель
        return"redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) { //Извлекаем id, который передаётся в адресе запроса и кладем его в аргумент id
        //Помещаем в модель текущего человека
        model.addAttribute("person", personDAO.show(id));//В кач-ве значения кладём то, что вернётся из personDAO по id
        //Представление
        return "people/edit";
    }//Помещаем id человека, который пришел из этого метода в HTML файл, используя метод getId

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult,
                         @PathVariable("id") int id) {//Принимаем объект person с помощью @ModelAttribute из формы
                                                      //Также принимаем значение id из адреса
          if(bindingResult.hasErrors())
              return "people/edit";

         personDAO.update(id, person); //Обновляем данные
         return "redirect:/people"; //Делаем redirect на адрес /people
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) { //Считываем id с помощью @PathVariable
        personDAO.delete(id);//Принимаем id человека, которого мы хотим удалить
        return "redirect:/people";  //После удалдения делаем redirect на указанный адрес
    }

}



