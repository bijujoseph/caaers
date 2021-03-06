/*******************************************************************************
 * Copyright SemanticBits, Northwestern University and Akaza Research
 * 
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caaers/LICENSE.txt for details.
 ******************************************************************************/
package gov.nih.nci.cabig.caaers.utils;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.acegisecurity.afterinvocation.BasicAclEntryAfterInvocationCollectionFilteringProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Biju Joseph Date: Sep 20, 2007 Time: 10:54:51 AM
 * 
 */
public class CollectionFilterer implements Filterer {
    // ~ Static fields/initializers
    // =====================================================================================

    protected static final Log logger = LogFactory
                    .getLog(BasicAclEntryAfterInvocationCollectionFilteringProvider.class);

    // ~ Instance fields
    // ================================================================================================

    private Collection collection;

    // collectionIter offers significant performance optimisations (as
    // per acegisecurity-developer mailing list conversation 19/5/05)
    private Iterator collectionIter;

    private List removeList;

    // ~ Constructors
    // ===================================================================================================

    public CollectionFilterer(Collection collection) {
        this.collection = collection;

        // We create a Set of objects to be removed from the Collection,
        // as ConcurrentModificationException prevents removal during
        // iteration, and making a new Collection to be returned is
        // problematic as the original Collection implementation passed
        // to the method may not necessarily be re-constructable (as
        // the Collection(collection) constructor is not guaranteed and
        // manually adding may lose sort order or other capabilities)
        removeList = new ArrayList();
    }

    // ~ Methods
    // ========================================================================================================

    /**
     * 
     * @see org.acegisecurity.afterinvocation.Filterer#getFilteredObject()
     */
    public Object getFilteredObject() {
        // Now the Iterator has ended, remove Objects from Collection
        Iterator removeIter = removeList.iterator();

        int originalSize = collection.size();

        while (removeIter.hasNext()) {
            collection.remove(removeIter.next());
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Original collection contained " + originalSize
                            + " elements; now contains " + collection.size() + " elements");
        }

        return collection;
    }

    /**
     * 
     * @see org.acegisecurity.afterinvocation.Filterer#iterator()
     */
    public Iterator iterator() {
        collectionIter = collection.iterator();

        return collectionIter;
    }

    /**
     * 
     * @see org.acegisecurity.afterinvocation.Filterer#remove(java.lang.Object)
     */
    public void remove(Object object) {
        removeList.add(object);
    }
}
