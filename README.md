# AEM Model List

A pattern implementation for handling components which are lists of 
resources.  

## On the Matter of Lists

Much of what is done in AEM is the production and rendering of lists. 
Take for example a list of links.  In this fairly common example each 
link's rendering may require a number of properties.  What is the link to?
Should it open a new tab?  Will there be query parameters or a hash fragment 
appended?  

The AEM Model List module proposes a pattern for such components whereby 
a container component, the LinkList in this case, manages individual item 
configurations, Link configurations in this case, as child resources where 
each resource is individually authorable.  This is in contrast to some 
common patterns like using multivalue properties or composite multivalue 
lists which create complex and confusing authoring experiences (subjective fact) 
which are hard to maintain and reason about and also do not truly express 
the nature of the children as unique resources.

## Usage

### Including in Your Project

These instructions presume usage of the multi-module project setup.

#### Include the modules as a dependency in your main POM

```xml
<dependency>
    <groupId>com.avionos.aem.modellist</groupId>
    <artifactId>aem-model-list.api</artifactId>
    <version>0.1.0</version>
    <scope>provided</scope>
</dependency>
<dependency>
    <groupId>com.avionos.aem.modellist</groupId>
    <artifactId>aem-model-list.ui.apps</artifactId>
    <version>0.1.0</version>
    <type>zip</type>
</dependency>
```

Once done you should include the API module as a dependency of your 
CORE module.

#### Include the UI Zip archive as a subPackage in your UI.Apps POM

```xml
<plugin>
    <groupId>com.day.jcr.vault</groupId>
    <artifactId>content-package-maven-plugin</artifactId>
    <extensions>true</extensions>
    <configuration>
        <filterSource>src/main/content/META-INF/vault/filter.xml</filterSource>
        <verbose>true</verbose>
        <failOnError>true</failOnError>
        <group>your-project-group</group>
        <embeddeds>
            <embedded>
                <groupId>com.your.project</groupId>
                <artifactId>your-project.core</artifactId>
                <target>/apps/your-project/install</target>
            </embedded>
        </embeddeds>
        <subPackages>
            <subPackage>
                <groupId>com.avionos.aem.modellist</groupId>
                <artifactId>aem-model-list.ui.apps</artifactId>
                <filter>true</filter>
            </subPackage>
        </subPackages>
    </configuration>
</plugin>
```

### Java API

The easiest way to utilize the pattern exposed by this module is to have 
your container component backing classes extend the `AbstractChildModelList` 
class.  

### Javascript API

#### `AEMModelList.Lists.v1`

##### `ListEditor`

###### `#addListItem( editable, item[, options ] )`

```javascript
AEMModelList.Lists.v1.ListEditor.addListItem( this, {
        "sling:resourceType": "my/resource/type"
    }, {
        "listItemNameBase": "myitem",
        "refresh": true
    } )
    .done( function() { 
        // Do something else... 
    } );
```

* `editable`: The AEM Editable object instance representing the container
  component.  When being used in the context of an edit config, `this` 
  refers to the editable.
* `item`: Properties of the item being added.  jcr:primaryType defaults to 
  "nt:unstructured" if left unset.
* `options`
  * `listName`: Optional - name of a child resource representing the list container. 
    If not provided then the provided `editable` is assumed to be the container.
  * `listResourceType`: Optional - the sling:resourceType of the container. 
    This will default to the true resource type but is useful in cases where 
    the container is backed by a synthetic resource.
  * `listPrimaryType`: Optional - similar to `listResourceType` but used to 
    indicate the `jcr:primaryType` of the container.  Defaults to `nt:unstructured`.
  * `listItemNameBase`: Optional - a base string to use when constructing the 
    name of the list item being added.  Defaults to "item".
  * `listItemName`: Optional - the full name of the list item to add.  Defaults 
    to `listItemNameBase + Date.now()`
  * `refresh`: Optional Boolean - indicates whether the container should 
    be refreshed after completion of the insertion operation. Defaults to 
    `false`.
    
###### `#moveForward( editable[, options ] )`

Used to swap the position of an editable with its next sibling.

* `editable`: The AEM Editable representing the list item being operated upon
* `options`
  * `refresh`: Optional Boolean - indicates whether the container should 
    be refreshed after successful movement.  Defaults to `false`.

###### `#moveBackward( editable[, options ] )`

Same as `#moveForward`, but swaps the item with its prior sibling.

##### `removeSelection()`

Utility method.  Deselects any editable(s) currently selected in the 
authoring UI.

## How to build

To build all the modules run in the project root directory the following command with Maven 3:

    mvn clean install

If you have a running AEM instance you can build and package the whole project and deploy into AEM with  

    mvn clean install -PautoInstallPackage
    
Or to deploy it to a publish instance, run

    mvn clean install -PautoInstallPackagePublish
    
Or alternatively

    mvn clean install -PautoInstallPackage -Daem.port=4503

Or to deploy only the bundle to the author, run

    mvn clean install -PautoInstallBundle
