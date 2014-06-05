package org.ontologyengineering.owltoy;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

import java.io.File;

/**
 * Create a simple ontology with a few classes, a property, and an
 * object property restriction
 */
public class Main {

    public static void simpleOntology() throws OWLOntologyCreationException, OWLOntologyStorageException {
        final OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        final OWLDataFactory factory = manager.getOWLDataFactory();

        final OWLOntology ontology  = manager.createOntology();

        final PrefixManager pm = new DefaultPrefixManager(
                "http://ontologyengineering.org/example/owltoy#");

        final OWLClass clsLecturer = factory.getOWLClass(":Lecturer", pm);
        final OWLClass clsModule = factory.getOWLClass(":Module", pm);
        final OWLProperty propTeaches = factory.getOWLObjectProperty(":teaches", pm);

        final OWLObjectPropertyExpression propExpTeaches =
                factory.getOWLObjectProperty(":teaches", pm);
        // ∀ teaches . Module
        final OWLClassExpression clsExpTeachesOnlyModules =
                factory.getOWLObjectAllValuesFrom(propExpTeaches, clsModule);

        manager.addAxiom(ontology,
                factory.getOWLDeclarationAxiom(clsLecturer));
        manager.addAxiom(ontology,
                factory.getOWLDeclarationAxiom(clsModule));
        manager.addAxiom(ontology,
                factory.getOWLDeclarationAxiom(propTeaches));
        manager.addAxiom(ontology, // Lecturer ⊑ ∀ teaches . Module
                factory.getOWLSubClassOfAxiom(clsLecturer, clsExpTeachesOnlyModules));

        final File file = new File("/tmp/local.owl");
        manager.saveOntology(ontology, IRI.create(file.toURI()));
    }
    public static void main(String [] args) throws Exception {
        simpleOntology();
    }

}
