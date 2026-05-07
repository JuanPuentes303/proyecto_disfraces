package com.disfracesrivera.service;

import com.disfracesrivera.dto.DisfrazDetalleView;
import com.disfracesrivera.dto.DisfrazRequest;
import com.disfracesrivera.model.Categoria;
import com.disfracesrivera.model.Disfraz;
import com.disfracesrivera.model.ImagenDisfraz;
import com.disfracesrivera.repository.CategoriaRepository;
import com.disfracesrivera.repository.DisfrazRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class DisfrazService {

    private final DisfrazRepository disfrazRepository;
    private final CategoriaRepository categoriaRepository;
    private final ArchivoService archivoService;

    public DisfrazService(
            DisfrazRepository disfrazRepository,
            CategoriaRepository categoriaRepository,
            ArchivoService archivoService
    ) {
        this.disfrazRepository = disfrazRepository;
        this.categoriaRepository = categoriaRepository;
        this.archivoService = archivoService;
    }

    public List<Disfraz> listarActivos() {
        return disfrazRepository.findByActivoTrue();
    }

    public List<Disfraz> obtenerAleatorios() {
        return disfrazRepository.obtenerSeisAleatorios();
    }

    public List<Disfraz> buscarDisfraces(String busqueda, Long categoriaId) {
        boolean tieneBusqueda = busqueda != null && !busqueda.trim().isEmpty();
        boolean tieneCategoria = categoriaId != null;

        if (tieneBusqueda && tieneCategoria) {
            return disfrazRepository.findByNombreContainingIgnoreCaseAndCategoriaIdAndActivoTrue(
                    busqueda.trim(),
                    categoriaId
            );
        }

        if (tieneBusqueda) {
            return disfrazRepository.findByNombreContainingIgnoreCaseAndActivoTrue(busqueda.trim());
        }

        if (tieneCategoria) {
            return disfrazRepository.findByCategoriaIdAndActivoTrue(categoriaId);
        }

        return disfrazRepository.findByActivoTrue();
    }

    public void crearDisfraz(DisfrazRequest request, MultipartFile imagen) {
        Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
                .orElseThrow(() -> new IllegalArgumentException("La categoría seleccionada no existe"));

        Disfraz disfraz = new Disfraz();
        disfraz.setNombre(request.getNombre());
        disfraz.setDescripcion(request.getDescripcion());
        disfraz.setTalla(request.getTalla());
        disfraz.setGenero(request.getGenero());
        disfraz.setPrecioAlquiler(request.getPrecioAlquiler());
        disfraz.setPrecioCompra(request.getPrecioCompra());
        disfraz.setDisponibleVenta(request.getDisponibleVenta() != null ? request.getDisponibleVenta() : false);
        disfraz.setCategoria(categoria);

        String rutaImagen = archivoService.guardarImagen(imagen);

        ImagenDisfraz imagenDisfraz = new ImagenDisfraz();
        imagenDisfraz.setUrlImagen(rutaImagen);
        imagenDisfraz.setPrincipal(true);
        imagenDisfraz.setDisfraz(disfraz);

        disfraz.getImagenes().add(imagenDisfraz);

        disfrazRepository.save(disfraz);
    }

    @Transactional(readOnly = true)
    public Disfraz obtenerPorId(Long id) {
        return disfrazRepository.buscarDetallePorId(id)
                .orElseThrow(() -> new IllegalArgumentException("El disfraz no existe"));
    }

    @Transactional(readOnly = true)
    public DisfrazDetalleView obtenerDetallePorId(Long id) {
        Disfraz disfraz = disfrazRepository.buscarDetallePorId(id)
                .orElseThrow(() -> new IllegalArgumentException("El disfraz no existe"));

        return convertirADetalleView(disfraz);
    }

    private DisfrazDetalleView convertirADetalleView(Disfraz disfraz) {
        return new DisfrazDetalleView(
                disfraz.getId(),
                disfraz.getNombre(),
                disfraz.getDescripcion(),
                disfraz.getTalla(),
                disfraz.getGenero(),
                disfraz.getPrecioAlquiler(),
                disfraz.getPrecioCompra(),
                disfraz.getCategoria() != null ? disfraz.getCategoria().getNombre() : "Sin categoría",
                obtenerImagenPrincipal(disfraz)
        );
    }

    private String obtenerImagenPrincipal(Disfraz disfraz) {
        if (disfraz.getImagenes() == null || disfraz.getImagenes().isEmpty()) {
            return null;
        }

        return disfraz.getImagenes()
                .stream()
                .filter(imagen -> Boolean.TRUE.equals(imagen.getPrincipal()))
                .findFirst()
                .orElse(disfraz.getImagenes().get(0))
                .getUrlImagen();
    }
}