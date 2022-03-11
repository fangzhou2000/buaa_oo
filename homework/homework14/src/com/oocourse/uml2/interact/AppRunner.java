package com.oocourse.uml2.interact;

import com.oocourse.uml2.interact.common.AttributeClassInformation;
import com.oocourse.uml2.interact.common.InstructionType;
import com.oocourse.uml2.interact.common.OperationParamInformation;
import com.oocourse.uml2.interact.common.OutputInformation;
import com.oocourse.uml2.interact.exceptions.InputArgumentException;
import com.oocourse.uml2.interact.exceptions.user.UserProcessException;
import com.oocourse.uml2.interact.format.UmlGeneralInteraction;
import com.oocourse.uml2.models.common.MessageSort;
import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.exceptions.UmlParseException;
import com.oocourse.uml2.utils.json.InputWithJson;
import org.json.simple.parser.ParseException;

import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AppRunner {
    private static final PrintStream DEFAULT_ERROR_STREAM = System.err;
    private static final InputStream DEFAULT_INPUT_STREAM = System.in;
    private static final PrintStream DEFAULT_OUTPUT_STREAM = System.out;
    private static final String END_OF_MODEL_REGEXP = "^\\s*END_OF_MODEL\\s*$";
    private static final String BLANK_LINE_REGEXP = "^\\s*$";
    private static final InputWithJson<UmlElement, UmlParseException> MODEL_LINE_PARSER = UmlElement::loadFromExportedJson;
    private static final Function<String, String> ENCRYPTION = OutputEncryption::getEncryptedMessage;
    private static final String WHITE_SPACE_SPLITTER = "\\s+";
    private static final int RUN_RE_EXIT_CODE = 192;
    private final Class<? extends UmlGeneralInteraction> interactionClass;
    private final Constructor<? extends UmlGeneralInteraction> constructor;
    private final InputStream inputStream;
    private final PrintStream outputStream;
    private final PrintStream errorStream;
    private final List<UmlElement> elementList;
    private UmlGeneralInteraction interaction = null;
    private final Map<InstructionType, ArgumentsProcessor<Exception>> PROCESSORS
            = Collections.unmodifiableMap(new HashMap<InstructionType, ArgumentsProcessor<Exception>>() {{
        put(InstructionType.CLASS_COUNT, AppRunner.this::runAsClassCount);
        put(InstructionType.CLASS_OPERATION_COUNT, AppRunner.this::runAsClassOperationCount);
        put(InstructionType.CLASS_ATTR_COUNT, AppRunner.this::runAsClassAttrCount);
        put(InstructionType.CLASS_OPERATION_VISIBILITY, AppRunner.this::runAsClassOperationVisibility);
        put(InstructionType.CLASS_ATTR_VISIBILITY, AppRunner.this::runAsClassAttrVisibility);
        put(InstructionType.CLASS_ATTR_TYPE, AppRunner.this::runAsClassAttrType);
        put(InstructionType.CLASS_OPERATION_PARAM_TYPE, AppRunner.this::runAsClassOperationParamType);
        put(InstructionType.CLASS_ASSO_CLASS_LIST, AppRunner.this::runAsClassAssoClassList);
        put(InstructionType.CLASS_IMPLEMENT_INTERFACE_LIST, AppRunner.this::runAsClassImplementInterfaceList);
        put(InstructionType.CLASS_TOP_BASE, AppRunner.this::runAsClassTopBase);
        put(InstructionType.CLASS_INFO_HIDDEN, AppRunner.this::runAsClassInfoHidden);

        put(InstructionType.STATE_COUNT, AppRunner.this::runAsStateCount);
        put(InstructionType.SUBSEQUENT_STATE_COUNT, AppRunner.this::runAsSubsequentStateCount);
        put(InstructionType.TRANSITION_TRIGGER, AppRunner.this::runAsTransitionTrigger);

        put(InstructionType.PTCP_OBJ_COUNT, AppRunner.this::runAsPtcpObjCount);
        put(InstructionType.INCOMING_MSG_COUNT, AppRunner.this::runAsIncomingMsgCount);
        put(InstructionType.SENT_MESSAGE_COUNT, AppRunner.this::runAsSentMessageCount);
    }});

    private AppRunningStatus status;

    private AppRunner(Class<? extends UmlGeneralInteraction> interactionClass,
                      InputStream inputStream, PrintStream outputStream, PrintStream errorStream)
            throws NoSuchMethodException {
        this.interactionClass = interactionClass;
        this.constructor = this.interactionClass.getConstructor(UmlElement[].class);
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.errorStream = errorStream;
        this.elementList = new ArrayList<>();
        this.status = AppRunningStatus.NOT_STARTED;
    }

    private AppRunner(Class<? extends UmlGeneralInteraction> interactionClass) throws NoSuchMethodException {
        this(interactionClass, DEFAULT_INPUT_STREAM, DEFAULT_OUTPUT_STREAM, DEFAULT_ERROR_STREAM);
    }

    public static AppRunner newInstance(Class<? extends UmlGeneralInteraction> interactionClass) throws NoSuchMethodException {
        return new AppRunner(interactionClass);
    }

    private static void printlnToStream(String message, PrintStream printStream) {
        printStream.println(ENCRYPTION.apply(message));
    }

    public Class<? extends UmlGeneralInteraction> getInteractionClass() {
        return interactionClass;
    }

    public void run(String[] args) {
        try {
            beforeStartEvent();
            Scanner scanner = new Scanner(inputStream);
            while (this.status.isRunning() && scanner.hasNextLine()) {
                String line = scanner.nextLine();
                lineProcessEvent(line);
            }
            scanner.close();
            afterCompleteEvent();
        } catch (Exception e) {
            exceptionProcessEvent(e);
        }
    }

    private void exceptionProcessEvent(Exception e) {
        printlnAsError("Something wrong with your process.");
        e.printStackTrace();
        System.exit(RUN_RE_EXIT_CODE);
    }

    private void beforeStartEvent() {
        this.status = AppRunningStatus.PROCESSING_MODEL;
    }

    private void afterCompleteEvent() {
        this.status = AppRunningStatus.COMPLETED;
    }

    private void lineProcessEvent(String line)
            throws Exception {
        if (!isBlankLine(line)) {
            if (this.status == AppRunningStatus.PROCESSING_MODEL) {
                if (isEndOfModel(line)) {
                    endOfModelProcessEvent();
                } else {
                    modelProcessEvent(line);
                }
            } else if (this.status == AppRunningStatus.PROCESSING_INSTRUCTION) {
                instructionProcessEvent(line);
            }
        }
    }

    private void modelProcessEvent(String line) throws UmlParseException, ParseException {
        UmlElement umlElement = MODEL_LINE_PARSER.loadFromString(line);
        this.elementList.add(umlElement);
    }

    private void endOfModelProcessEvent()
            throws IllegalAccessException, InstantiationException, InvocationTargetException {
        UmlElement[] elements = new UmlElement[this.elementList.size()];
        for (int i = 0; i < this.elementList.size(); i++) {
            elements[i] = this.elementList.get(i);
        }

        interaction = newInteractionInstance(elements);
        this.status = AppRunningStatus.PROCESSING_INSTRUCTION;
    }

    private void instructionProcessEvent(String line) throws Exception {
        List<Object> arguments;
        try {
            arguments = InstructionType.COMMON_PARSER.parse(line.trim(), WHITE_SPACE_SPLITTER);
        } catch (InputArgumentException e) {
            printlnAsError("Error, invalid instruction type.");
            return;
        }

        InstructionType type = (InstructionType) arguments.get(0);
        runAsArguments(type, line);
    }

    private void runAsArguments(InstructionType type, String line) throws Exception {
        List<Object> arguments;
        try {
            arguments = InstructionType.INSTRUCTION_PARSERS.get(type).parse(line.trim(), WHITE_SPACE_SPLITTER);
        } catch (InputArgumentException e) {
            printlnAsError("Error, invalid instruction format.");
            return;
        }

        if (!PROCESSORS.containsKey(type)) {
            printlnAsError("Sorry, processors not supported by app engine yet.");
            return;
        }

        OutputInformation information;
        try {
            information = PROCESSORS.get(type).process(type, arguments);
        } catch (UserProcessException exception) {
            printlnAsOutput(exception.getMessage());
            return;
        }

        printlnAsOutput(information.getMessage());
    }

    private UmlGeneralInteraction newInteractionInstance(UmlElement[] elements)
            throws IllegalAccessException, InstantiationException, InvocationTargetException {
        return this.constructor.newInstance(new Object[]{elements});
    }

    private boolean isEndOfModel(String line) {
        return (line != null) && (line.matches(END_OF_MODEL_REGEXP));
    }

    private boolean isBlankLine(String line) {
        return (line != null) && (line.matches(BLANK_LINE_REGEXP));
    }

    private void printlnAsOutput(String message) {
        printlnToStream(message, this.outputStream);
    }

    private void printlnAsError(String message) {
        printlnToStream(message, this.errorStream);
    }

    private OutputInformation runAsClassCount(InstructionType type, List<Object> arguments) {
        int count = interaction.getClassCount();
        return new OutputInformation(
                String.format("Total class count is %s.", count)
        );
    }

    private OutputInformation runAsClassOperationCount(InstructionType type, List<Object> arguments) throws Exception {
        String className = (String) arguments.get(1);
        int count = interaction.getClassOperationCount(className);
        return new OutputInformation(
                String.format("Ok, operation count of class \"%s\" is %s.", className, count)
        );
    }

    private OutputInformation runAsClassAttrCount(InstructionType type, List<Object> arguments) throws Exception {
        String className = (String) arguments.get(1);
        int count = interaction.getClassAttributeCount(className);
        return new OutputInformation(
                String.format("Ok, attribute count of class \"%s\" is %s.", className, count)
        );
    }

    private OutputInformation runAsClassOperationVisibility(InstructionType type, List<Object> arguments)
        throws Exception {
        String className = (String) arguments.get(1);
        String methodName = (String) arguments.get(2);
        Map<Visibility, Integer> analysis = interaction.getClassOperationVisibility(className, methodName);
        return new OutputInformation(
            String.format(
                "Ok, operation visibility of method \"%s\" in class \"%s\" is %s.",
                methodName, className,
                Arrays.stream(Visibility.values())
                    .map(visibility -> String.format("%s: %s",
                        visibility.getCommonString(),
                        analysis.getOrDefault(visibility, 0)))
                    .collect(Collectors.joining(", "))
            )
        );
    }

    private OutputInformation runAsClassAttrVisibility(InstructionType type, List<Object> arguments) throws Exception {
        String className = (String) arguments.get(1);
        String attributeName = (String) arguments.get(2);
        Visibility visibility = interaction.getClassAttributeVisibility(className, attributeName);
        return new OutputInformation(
            String.format(
                "Ok, attribute \"%s\" in class \"%s\"'s visibility is %s.",
                attributeName, className, visibility.getCommonString()
            )
        );
    }

    private OutputInformation runAsClassAttrType(InstructionType type, List<Object> arguments) throws Exception {
        String className = (String) arguments.get(1);
        String attributeName = (String) arguments.get(2);
        String attrType = interaction.getClassAttributeType(className, attributeName);
        return new OutputInformation(
            String.format(
                "Ok, the type of attribute \"%s\" in class \"%s\" is %s.",
                attributeName, className, attrType
            )
        );
    }

    private OutputInformation runAsClassOperationParamType(InstructionType type, List<Object> arguments) throws Exception {
        String className = (String) arguments.get(1);
        String operationName = (String) arguments.get(2);
        List<OperationParamInformation> paramTypes = interaction.getClassOperationParamType(className, operationName);
        Collections.sort(paramTypes);
        return new OutputInformation(
            String.format(
                "Ok, method \"%s\" in class \"%s\" has parameter tables and return value: %s.",
                operationName, className,
                paramTypes.stream()
                    .map(OperationParamInformation::toString)
                .collect(Collectors.joining(", "))
            )
        );
    }

    private OutputInformation runAsClassAssoClassList(InstructionType type, List<Object> arguments) throws Exception {
        String className = (String) arguments.get(1);
        List<String> classList = new ArrayList<>(interaction.getClassAssociatedClassList(className));
        Collections.sort(classList);
        return new OutputInformation(
            String.format(
                "Ok, associated classes of class \"%s\" are (%s).",
                className,
                String.join(", ", classList)
            )
        );
    }

    private OutputInformation runAsClassImplementInterfaceList(InstructionType type, List<Object> arguments) throws Exception {
        String className = (String) arguments.get(1);
        List<String> interfaces = new ArrayList<>(interaction.getImplementInterfaceList(className));
        Collections.sort(interfaces);
        return new OutputInformation(
            String.format(
                "Ok, implement interfaces of class \"%s\" are (%s).",
                className,
                String.join(", ", interfaces)
            )
        );
    }

    private OutputInformation runAsClassTopBase(InstructionType type, List<Object> arguments) throws Exception {
        String className = (String) arguments.get(1);
        String topBaseClassName = interaction.getTopParentClass(className);
        return new OutputInformation(
            String.format("Ok, top base class of class \"%s\" is %s.", className, topBaseClassName)
        );
    }

    private OutputInformation runAsClassInfoHidden(InstructionType type, List<Object> arguments) throws Exception {
        String className = (String) arguments.get(1);
        List<AttributeClassInformation> attributes = new ArrayList<>(interaction.getInformationNotHidden(className));
        Collections.sort(attributes);
        if (attributes.isEmpty()) {
            return new OutputInformation(
                String.format("Yes, information of class \"%s\" is hidden.", className)
            );
        } else {
            return new OutputInformation(
                String.format(
                    "No, attribute %s, are not hidden.",
                    attributes.stream()
                        .map(information -> String.format("%s in %s",
                            information.getAttributeName(), information.getClassName()))
                        .collect(Collectors.joining(", "))
                )
            );
        }
    }

    private OutputInformation runAsStateCount(InstructionType type, List<Object> arguments) throws Exception {
        String stateMachineName = (String) arguments.get(1);
        int count = interaction.getStateCount(stateMachineName);
        return new OutputInformation(
            String.format("Ok，state count of statemachine \"%s\" is %s.", stateMachineName, count)
        );
    }

    private OutputInformation runAsSubsequentStateCount(InstructionType type, List<Object> arguments) throws Exception {
        String stateMachineName = (String) arguments.get(1);
        String stateName = (String) arguments.get(2);
        int count = interaction.getSubsequentStateCount(stateMachineName, stateName);
        return new OutputInformation(
            String.format("Ok, subsequent state count from state \"%s\" in statemachine \"%s\" is %s.",
                stateName, stateMachineName, count)
        );
    }

    private OutputInformation runAsTransitionTrigger(InstructionType instructionType, List<Object> arguments) throws Exception {
        String stateMachineName = (String) arguments.get(1);
        String sourceStateName = (String) arguments.get(2);
        String targetStateName = (String) arguments.get(3);
        List<String> triggers = interaction.getTransitionTrigger(stateMachineName, sourceStateName, targetStateName);
        Collections.sort(triggers);
        return new OutputInformation(
            String.format("Ok, triggers of transition from state \"%s\" to state \"%s\" in statemachine \"%s\" are (%s).",
                sourceStateName, targetStateName, stateMachineName, String.join(", ", triggers)
            )
        );
    }

    private OutputInformation runAsPtcpObjCount(InstructionType type, List<Object> arguments) throws Exception {
        String interactionName = (String) arguments.get(1);
        int count = interaction.getParticipantCount(interactionName);
        return new OutputInformation(
            String.format("Ok, participant count of umlinteraction \"%s\" is %s.",
                interactionName, count)
        );
    }

    private OutputInformation runAsIncomingMsgCount(InstructionType type, List<Object> arguments) throws Exception {
        String interactionName = (String) arguments.get(1);
        String lifelineName = (String) arguments.get(2);
        int count = interaction.getIncomingMessageCount(interactionName, lifelineName);
        return new OutputInformation(
            String.format("Ok, incoming message count of lifeline \"%s\" in umlinteraction \"%s\" is %s.",
                lifelineName, interactionName, count)
        );
    }

    private OutputInformation runAsSentMessageCount(InstructionType instructionType, List<Object> arguments) throws Exception {
        String interactionName = (String) arguments.get(1);
        String lifelineName = (String) arguments.get(2);
        MessageSort sort = (MessageSort) arguments.get(3);
        int count = interaction.getSentMessageCount(interactionName, lifelineName, sort);
        return new OutputInformation(
            String.format("Ok, sent message count of lifeline \"%s\" of umlinteraction \"%s\" is %s.",
                lifelineName, interactionName, count)
        );
    }

    private enum AppRunningStatus {
        NOT_STARTED,
        PROCESSING_MODEL,
        PROCESSING_INSTRUCTION,
        COMPLETED,
        EXIT;

        boolean isStarted() {
            return (this != NOT_STARTED);
        }

        boolean isRunning() {
            return isStarted() && !isFinished();
        }

        boolean isFinished() {
            return (this == COMPLETED) || (this == EXIT);
        }
    }

    private interface ArgumentsProcessor<E extends Exception> {
        OutputInformation process(InstructionType type, List<Object> arguments) throws E;
    }
}
