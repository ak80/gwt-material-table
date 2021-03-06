package gwt.material.design.client.data.component;

/*
 * #%L
 * GwtMaterial
 * %%
 * Copyright (C) 2015 - 2016 GwtMaterialDesign
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Ben Dol
 */
public class Components<T extends Component<?>> extends ArrayList<T> {

    public interface Clone<T> {
        T clone(T clone);
    }

    private int maxSize = -1;

    public Components() {
    }

    public Components(int maxSize) {
        this.maxSize = maxSize;
    }

    public Components(Collection<? extends T> components) {
        this(components, null);
    }

    public Components(Collection<? extends T> components, Clone<T> clone) {
        super(components);

        if(clone != null) {
            Components<T> cloned = new Components<>();
            for(T component : this) {
                cloned.add(clone.clone(component));
            }
            super.clear();
            addAll(cloned);
        }
    }

    @Override
    public void clear() {
        clearElements();
        super.clear();
    }

    public void clearElements() {
        // clear dom rows
        for(T component : this) {
            component.clearElement();
        }
    }

    public void clearComponents() {
        super.clear();
    }

    public boolean isFull() {
        return maxSize != -1 && size() == maxSize;
    }

    @Override
    public boolean add(T t) {
        return !isFull() && super.add(t);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return canAdd(c) && super.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        return canAdd(c) && super.addAll(index, c);
    }

    @Override
    public T remove(int index) {
        T component = super.get(index);
        if(component != null) {
            component.clearElement();
        }
        return softRemove(index);
    }

    public T softRemove(int index) {
        return super.remove(index);
    }

    public boolean softRemove(T item) {
        int i = indexOf(item);
        if (i == -1) {
            return false;
        }
        super.remove(i);
        return true;
    }

    public boolean canAdd(Collection<? extends T> c) {
        return !isFull() && (maxSize == -1 || (c.size() + size()) <= maxSize);
    }
}
