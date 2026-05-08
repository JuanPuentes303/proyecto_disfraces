package com.disfracesrivera.service;

import com.disfracesrivera.dto.DisfrazDetalleView;
import com.disfracesrivera.dto.DisfrazPublicView;
import com.disfracesrivera.dto.DisfrazAdminView;
import com.disfracesrivera.dto.DisfrazRequest;
import com.disfracesrivera.model.Categoria;
import com.disfracesrivera.model.Disfraz;
import com.disfracesrivera.model.ImagenDisfraz;
import com.disfracesrivera.repository.CategoriaRepository;
import com.disfracesrivera.repository.DisfrazRepository;
import com.disfracesrivera.repository.ImagenDisfrazRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DisfrazService {

    private final DisfrazRepository disfrazRepository;
    private final CategoriaRepository categoriaRepository;
    private final ArchivoService archivoService;
    private final ImagenDisfrazRepository imagenDisfrazRepository;

    public DisfrazService(
            DisfrazRepository disfrazRepository,
            CategoriaRepository categoriaRepository,
            ArchivoService archivoService,
            ImagenDisfrazRepository imagenDisfrazRepository
    ) {
        this.disfrazRepository = disfrazRepository;
        this.categoriaRepository = categoriaRepository;
        this.archivoService = archivoService;
        this.imagenDisfrazRepository = imagenDisfrazRepository;
    }

    public List<Disfraz> listarActivos() {
        return disfrazRepository.findByActivoTrue();
    }

    public List<Disfraz> obtenerAleatorios() {
        return disfrazRepository.obtenerSeisAleatorios();
    }

    public List<Disfraz> buscarDisfraces(
            String busqueda,
            Long categoriaId,
            String talla,
            String genero,
            BigDecimal precioMin,
            BigDecimal precioMax
    ) {
        return disfrazRepository.buscarConFiltros(
                limpiarTexto(busqueda),
                categoriaId,
                limpiarTexto(talla),
                limpiarTexto(genero),
                precioMin,
                precioMax
        );
    }

    private String limpiarTexto(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            return null;
        }

        return valor.trim();
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

    @Transactional(readOnly = true)
    public DisfrazRequest obtenerRequestParaEditar(Long id) {
        Disfraz disfraz = disfrazRepository.buscarDetallePorId(id)
                .orElseThrow(() -> new IllegalArgumentException("El disfraz no existe"));

        DisfrazRequest request = new DisfrazRequest();
        request.setNombre(disfraz.getNombre());
        request.setDescripcion(disfraz.getDescripcion());
        request.setTalla(disfraz.getTalla());
        request.setGenero(disfraz.getGenero());
        request.setPrecioAlquiler(disfraz.getPrecioAlquiler());
        request.setPrecioCompra(disfraz.getPrecioCompra());
        request.setDisponibleVenta(disfraz.getDisponibleVenta());

        if (disfraz.getCategoria() != null) {
            request.setCategoriaId(disfraz.getCategoria().getId());
        }

        return request;
    }

    @Transactional
    public void actualizarDisfraz(Long id, DisfrazRequest request, MultipartFile imagen) {
        Disfraz disfraz = disfrazRepository.buscarDetallePorId(id)
                .orElseThrow(() -> new IllegalArgumentException("El disfraz no existe"));

        Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
                .orElseThrow(() -> new IllegalArgumentException("La categoría seleccionada no existe"));

        disfraz.setNombre(request.getNombre());
        disfraz.setDescripcion(request.getDescripcion());
        disfraz.setTalla(request.getTalla());
        disfraz.setGenero(request.getGenero());
        disfraz.setPrecioAlquiler(request.getPrecioAlquiler());
        disfraz.setPrecioCompra(request.getPrecioCompra());
        disfraz.setDisponibleVenta(request.getDisponibleVenta() != null ? request.getDisponibleVenta() : false);
        disfraz.setCategoria(categoria);

        if (imagen != null && !imagen.isEmpty()) {
            cambiarImagenPrincipal(disfraz, imagen);
        }

        disfrazRepository.save(disfraz);
    }

    @Transactional
    public void desactivarDisfraz(Long id) {
        Disfraz disfraz = disfrazRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El disfraz no existe"));

        disfraz.setActivo(false);
        disfrazRepository.save(disfraz);
    }

    private void cambiarImagenPrincipal(Disfraz disfraz, MultipartFile imagen) {
        String rutaImagen = archivoService.guardarImagen(imagen);

        disfraz.getImagenes().clear();

        ImagenDisfraz nuevaImagen = new ImagenDisfraz();
        nuevaImagen.setUrlImagen(rutaImagen);
        nuevaImagen.setPrincipal(true);
        nuevaImagen.setDisfraz(disfraz);

        disfraz.getImagenes().add(nuevaImagen);
    }

    @Transactional(readOnly = true)
    public List<DisfrazAdminView> listarTodosAdmin() {
        return disfrazRepository.listarTodosConDetalle()
                .stream()
                .map(this::convertirAAdminView)
                .toList();
    }

    private DisfrazAdminView convertirAAdminView(Disfraz disfraz) {
        return new DisfrazAdminView(
                disfraz.getId(),
                disfraz.getNombre(),
                disfraz.getDescripcion(),
                disfraz.getTalla(),
                disfraz.getGenero(),
                disfraz.getPrecioAlquiler(),
                disfraz.getPrecioCompra(),
                disfraz.getDisponibleVenta(),
                disfraz.getActivo(),
                disfraz.getCategoria() != null ? disfraz.getCategoria().getNombre() : "Sin categoría",
                obtenerImagenPrincipal(disfraz)
        );
    }

    @Transactional(readOnly = true)
    public List<DisfrazPublicView> listarPublicoConFiltros(
            String busqueda,
            Long categoriaId,
            String talla,
            String genero,
            BigDecimal precioMin,
            BigDecimal precioMax
    ) {
        return disfrazRepository.buscarConFiltrosDetalle(
                        limpiarTexto(busqueda),
                        categoriaId,
                        limpiarTexto(talla),
                        limpiarTexto(genero),
                        precioMin,
                        precioMax
                )
                .stream()
                .map(this::convertirAPublicView)
                .toList();
    }

    @Transactional(readOnly = true)
    public DisfrazPublicView obtenerPublicoPorId(Long id) {
        Disfraz disfraz = disfrazRepository.buscarDetallePorId(id)
                .orElseThrow(() -> new IllegalArgumentException("El disfraz no existe"));

        if (!Boolean.TRUE.equals(disfraz.getActivo())) {
            throw new IllegalArgumentException("El disfraz no está disponible");
        }

        return convertirAPublicView(disfraz);
    }

    private DisfrazPublicView convertirAPublicView(Disfraz disfraz) {
        return new DisfrazPublicView(
                disfraz.getId(),
                disfraz.getNombre(),
                disfraz.getDescripcion(),
                disfraz.getTalla(),
                disfraz.getGenero(),
                disfraz.getPrecioAlquiler(),
                disfraz.getPrecioCompra(),
                disfraz.getDisponibleVenta(),
                disfraz.getCategoria() != null ? disfraz.getCategoria().getNombre() : "Sin categoría",
                obtenerImagenPrincipal(disfraz)
        );
    }
}