package com.co.web.avanzada.controler;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.co.web.avanzada.entity.Inventario;
import com.co.web.avanzada.entity.Producto;
import com.co.web.avanzada.repository.IBodegaRepo;
import com.co.web.avanzada.repository.ICategoriaRepo;
import com.co.web.avanzada.repository.IInventarioRepo;
import com.co.web.avanzada.repository.IProductoRepo;

@Controller
public class InventarioController {
		@Autowired
		private IInventarioRepo iInventarioRepo;
		@Autowired
		private IBodegaRepo iBodegaRepo;
		@Autowired
		private IProductoRepo iProductoRepo;
		@Autowired
		private ICategoriaRepo iCategoriaRepo;
		
	 	@GetMapping("/addInventario/{idBodega}")
	    public String showSignUpForm(Model model, @PathVariable int idBodega) {
	    	/*Se le agrega a la plantilla un modelo vacio del been al que vamos a hacer una inserción*/
	    	model.addAttribute("inventario", new Inventario());
	    	/*Se le agrega a la plantilla un modelo de paises ya que requerimos de ellos para poder hacer una inserción.*/
	    	model.addAttribute("bodega", iBodegaRepo.findById(idBodega).get());
	    	/*Se le agrega a la plantilla un modelo de departamentos ya que requerimos de ellos para poder hacer una inserción.*/
	    	model.addAttribute("productos", new Producto());
	    	model.addAttribute("categorias", iCategoriaRepo.findAll());
	     	/*Se retorna la plantilla o formulario html para el registro del municipio*/
	    	return "add-inventario";
	    }
	    
	    /*Se reciben y se validan todos los datos del formulario mediante las anotaciones postMapping y validated, con el blinding result se manejan los resultados
	     * de la inserción de los datos.*/
	    @PostMapping("/add_inventario")
	    public String addInventario(@Validated Inventario inventario, BindingResult result, Model model) {
	        if (result.hasErrors()) {
	        	model.addAttribute("inventario", new Inventario());
	        	model.addAttribute("bodega", iBodegaRepo.findById(inventario.getBodega().getIdBodega()).get());
	        	model.addAttribute("productos", new Producto());
	        	model.addAttribute("categorias", iCategoriaRepo.findAll());
	            return "redirect:/addInventario/"+inventario.getBodega().getIdBodega();
	        }
	        /*Mediante el método .save del repositorio se guardan los datos despues de pasar todas las validaciones.*/
	        iInventarioRepo.save(inventario);
	        /* Se cargan todas los municipios existentes en la base de datos al modelo para poder listarlas.*/
	        model.addAttribute("inventario", iInventarioRepo.findByBodegaId(inventario.getBodega().getIdBodega()));
	        return "redirect:/listarInventario/"+inventario.getBodega().getIdBodega();
	    }
	    @GetMapping("/ajax/productos")
		public String ajaxProductos(@RequestParam("id_categoria") String id_categoria, Model model) {
	    	int id = Integer.parseInt(id_categoria);
			model.addAttribute("productos", iProductoRepo.findByCategoria(id));
			System.out.println(iProductoRepo.findByCategoria(id));
			return "add-inventario :: productos";
			
		}
	    /*En este método se recibe del formulario en donde se listan los municipios el id del municipio a editar, este id se
	     * se busca en la base de datos y se carga su información en el modelo o formulario de modificación del municipio.*/
	    @GetMapping("/listarInventario/{idBodega}")
		public String ListarInventario(Model model, @PathVariable("idBodega") String idBodega) {
			/*Se buscan el inventario mediante el método del repositorio findbybodegaid y se cargan en la variable 'inventario' 
	    	 * a la plantilla o medelo de la plantilla.*/
			List<Inventario> inventario = iInventarioRepo.findByBodegaId(Integer.parseInt(idBodega));
			model.addAttribute("inventario", inventario);
			model.addAttribute("bodega", iBodegaRepo.findById(Integer.parseInt(idBodega)).get());
			return "listarInventario";
		}
	    
}
