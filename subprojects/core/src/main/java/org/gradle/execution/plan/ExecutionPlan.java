/*
 * Copyright 2012 the original author or authors.
 *
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
 */

package org.gradle.execution.plan;

import org.gradle.api.Describable;
import org.gradle.api.Task;
import org.gradle.api.specs.Spec;

import javax.annotation.concurrent.NotThreadSafe;
import java.io.Closeable;
import java.util.Collection;
import java.util.function.Consumer;

/**
 * Represents a mutable graph of dependent work items.
 *
 * <p>The methods of this interface are not thread safe.
 */
@NotThreadSafe
public interface ExecutionPlan extends Describable, Closeable {
    void useFilter(Spec<? super Task> filter);

    void setContinueOnFailure(boolean continueOnFailure);

    void setScheduledNodes(Collection<? extends Node> nodes);

    void addEntryTasks(Collection<? extends Task> tasks);

    void addEntryTasks(Collection<? extends Task> tasks, int ordinal);

    /**
     * Returns the current contents of this plan. Note that this may change.
     */
    QueryableExecutionPlan getContents();

    /**
     * Calculates the execution plan for the current entry tasks. May be called multiple times.
     */
    void determineExecutionPlan();

    /**
     * Finalizes this plan once all nodes have been added. Must be called after {@link #determineExecutionPlan()}.
     */
    FinalizedExecutionPlan finalizePlan();

    /**
     * Invokes the given action when a task completes (as per {@link Node#isComplete()}). Does nothing for tasks that have already completed.
     */
    void onComplete(Consumer<LocalTaskNode> handler);

    /**
     * Overridden to remove IOException.
     */
    @Override
    void close();
}
