package org.goobi.api.rest;

import java.sql.SQLException;
import java.util.List;

import de.intranda.counterscript.model.MetadataInformation;
import de.intranda.counterscript.persistence.MetadataInformationManager;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Path("/counterscript/xml")
public class XmlResource {

    @GET
    @Produces(MediaType.TEXT_XML)
    public List<MetadataInformation> getXml() {

        List<MetadataInformation> mdl = null;
        try {
            mdl = MetadataInformationManager.calculateDataDate(null, null, false);
        } catch (SQLException e) {
            log.error(e);
        }

        return mdl;
    }

    @GET
    @Path("/{startdate}/{enddate}")
    @Produces(MediaType.TEXT_XML)
    public List<MetadataInformation> getXml(@PathParam("startdate") String start, @PathParam("enddate") String end) {

        List<MetadataInformation> mdl = null;
        try {
            mdl = MetadataInformationManager.calculateDataString(start, end, false);
        } catch (SQLException e) {
            log.error(e);
        }

        return mdl;
    }

    @GET
    @Path("/bnumber/{number}")
    @Produces(MediaType.TEXT_XML)
    public List<MetadataInformation> getXml(@PathParam("number") String number) {

        List<MetadataInformation> mdl = null;
        try {
            mdl = MetadataInformationManager.calculateDataForIdentifier(number);
        } catch (SQLException e) {
            log.error(e);
        }

        return mdl;
    }

    @Path("/withinactive")
    @GET
    @Produces(MediaType.TEXT_XML)
    public List<MetadataInformation> getXmlWithInactive() {

        List<MetadataInformation> mdl = null;
        try {
            mdl = MetadataInformationManager.calculateDataDate(null, null, true);
        } catch (SQLException e) {
            log.error(e);
        }

        return mdl;
    }

    @GET
    @Path("/withinactive/{startdate}/{enddate}")
    @Produces(MediaType.TEXT_XML)
    public List<MetadataInformation> getXmlWithInactive(@PathParam("startdate") String start, @PathParam("enddate") String end) {

        List<MetadataInformation> mdl = null;
        try {
            mdl = MetadataInformationManager.calculateDataString(start, end, true);
        } catch (SQLException e) {
            log.error(e);
        }

        return mdl;
    }

}
