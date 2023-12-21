package org.iesvdm.tests;

import org.iesvdm.streams.*;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.Comparator.comparing;
import static org.junit.jupiter.api.Assertions.fail;

class StreamsTest {

    @Test
    void test() {
        fail("Not yet implemented");
    }


    @Test
    void testSkeletonCliente() {

        ClienteHome cliHome = new ClienteHome();

        try {
            cliHome.beginTransaction();

            List<Cliente> list = cliHome.findAll();
            list.forEach(System.out::println);


            //TODO STREAMS


            cliHome.commitTransaction();
        } catch (RuntimeException e) {
            cliHome.rollbackTransaction();
            throw e; // or display error message
        }
    }


    @Test
    void testSkeletonComercial() {

        ComercialHome comHome = new ComercialHome();
        try {
            comHome.beginTransaction();

            List<Comercial> list = comHome.findAll();
            list.forEach(System.out::println);
            //TODO STREAMS

            comHome.commitTransaction();
        } catch (RuntimeException e) {
            comHome.rollbackTransaction();
            throw e; // or display error message
        }

    }

    @Test
    void testSkeletonPedido() {

        PedidoHome pedHome = new PedidoHome();
        try {
            pedHome.beginTransaction();

            List<Pedido> list = pedHome.findAll();
            list.forEach(System.out::println);

            //TODO STREAMS

            pedHome.commitTransaction();
        } catch (RuntimeException e) {
            pedHome.rollbackTransaction();
            throw e; // or display error message
        }

    }

    /**
     * 1. Devuelve un listado de todos los pedidos que se realizaron durante el año 2017,
     * cuya cantidad total sea superior a 500€.
     *
     * @throws ParseException
     */
    @Test
    void test1() throws ParseException {


        PedidoHome pedHome = new PedidoHome();
        try {
            pedHome.beginTransaction();

            //PISTA: Generación por sdf de fechas
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date ultimoDia2016 = sdf.parse("2016-12-31");
            Date primerDia2018 = sdf.parse("2018-01-01");

            List<Pedido> list = pedHome.findAll();

            //TODO STREAMS

            var listaPed = list.stream()
                    .filter(pedido -> pedido.getFecha().after(ultimoDia2016) && pedido.getFecha().before(primerDia2018) && pedido.getTotal() > 500);

            pedHome.commitTransaction();
        } catch (RuntimeException e) {
            pedHome.rollbackTransaction();
            throw e; // or display error message
        }

    }


    /**
     * 2. Devuelve un listado con los identificadores de los clientes que NO han realizado algún pedido.
     */
    @Test
    void test2() {

        ClienteHome cliHome = new ClienteHome();

        try {
            cliHome.beginTransaction();

            List<Cliente> list = cliHome.findAll();
            var clienteNoPed = list.stream().filter(cliente -> cliente.getPedidos().isEmpty()).map(Cliente::getId);

            cliHome.commitTransaction();
        } catch (RuntimeException e) {
            cliHome.rollbackTransaction();
            throw e; // or display error message
        }

    }

    /**
     * 3. Devuelve el valor de la comisión de mayor valor que existe en la tabla comercial
     */
    @Test
    void test3() {

        ComercialHome comHome = new ComercialHome();
        try {
            comHome.beginTransaction();

            List<Comercial> list = comHome.findAll();
            var valoAlto = list.stream().reduce((t1, t2) -> t1.getComisión() > t2.getComisión() ? t1 : t2);

            comHome.commitTransaction();
        } catch (RuntimeException e) {
            comHome.rollbackTransaction();
            throw e; // or display error message
        }

    }

    /**
     * 4. Devuelve el identificador, nombre y primer apellido de aquellos clientes cuyo segundo apellido no es NULL.
     * El listado deberá estar ordenado alfabéticamente por apellidos y nombre.
     */
    @Test
    void test4() {

        ClienteHome cliHome = new ClienteHome();

        try {
            cliHome.beginTransaction();

            List<Cliente> list = cliHome.findAll();

            var ola = list.stream()
                    .filter(cliente -> cliente.getApellido1() != null)
                    .sorted(comparing((Cliente cliente) -> cliente.getApellido2()).thenComparing((Cliente cli) -> cli.getNombre()))
                    .map(cliente -> cliente.getNombre() + cliente.getApellido1());

            cliHome.commitTransaction();
        } catch (RuntimeException e) {
            cliHome.rollbackTransaction();
            throw e; // or display error message
        }

    }

    /**
     * 5. Devuelve un listado con los nombres de los comerciales que terminan por "el" o "o".
     * Tenga en cuenta que se deberán eliminar los nombres repetidos.
     */
    @Test
    void test5() {

        ComercialHome comHome = new ComercialHome();
        try {
            comHome.beginTransaction();

            List<Comercial> list = comHome.findAll();

            var unicos = list.stream().filter(comercial ->
                            comercial.getNombre().substring(comercial.getNombre().length() - 2, comercial.getNombre().length()).equals("el")
                                    || comercial.getNombre().substring(comercial.getNombre().length() - 1, comercial.getNombre().length()).equals("o"))
                    .map(comercial -> comercial.getNombre())
                    .distinct();

            comHome.commitTransaction();
        } catch (RuntimeException e) {
            comHome.rollbackTransaction();
            throw e; // or display error message
        }

    }


    /**
     * 6. Devuelve un listado de todos los clientes que realizaron un pedido durante el año 2017, cuya cantidad esté entre 300 € y 1000 €.
     */
    @Test
    void test6() {

        PedidoHome pedHome = new PedidoHome();
        try {
            pedHome.beginTransaction();

            List<Pedido> list = pedHome.findAll();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date ultimoDia2016 = sdf.parse("2016-12-31");
            Date primerDia2018 = sdf.parse("2018-01-01");

            //TODO STREAMS
            var pedidos = list.stream()
                    .filter(pedido -> pedido.getFecha().after(ultimoDia2016) && pedido.getFecha().before(primerDia2018) && pedido.getTotal() > 300 && pedido.getTotal() > 1000)
                    .map(pedido -> pedido.getCliente());

            pedidos.forEach(cliente -> System.out.println(cliente));
            pedHome.commitTransaction();
        } catch (RuntimeException e) {
            pedHome.rollbackTransaction();
            throw e; // or display error message
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }


    /**
     * 7. Calcula la media del campo total de todos los pedidos realizados por el comercial Daniel Sáez
     */
    @Test
    void test7() {

        ComercialHome comHome = new ComercialHome();
        try {
            comHome.beginTransaction();

            List<Comercial> list = comHome.findAll();

            var totalmedia = list.stream()
                    .filter(comercial -> comercial.getNombre().equals("Daniel") && comercial.getApellido1().equals("Sáez"))
                    .map(comercial -> {
                                Set<Pedido> pedidos = comercial.getPedidos();
                                Optional<Double[]> reduceArrayDouble = pedidos.stream().map(p -> new Double[]{p.getTotal(), 1.0})
                                        .reduce((doubles, doubles2) -> new Double[]{
                                                doubles[0] + doubles2[0],
                                                doubles[1] + doubles2[1]
                                        });

                                if (reduceArrayDouble.isPresent()) {
                                    var calculos = reduceArrayDouble.get();

                                    return String.format("Media %8.2f \n", (calculos[0] / calculos[1]));
                                }
                                return String.format("%-20s", comercial.getNombre()) + " No tiene pedidos" + "\n";
                            }
                    );

            comHome.commitTransaction();
        } catch (RuntimeException e) {
            comHome.rollbackTransaction();
            throw e; // or display error message
        }

    }
}
