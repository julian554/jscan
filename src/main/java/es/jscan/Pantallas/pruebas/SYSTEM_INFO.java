package es.jscan.Pantallas.pruebas;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Union;
import com.sun.jna.ptr.IntByReference;
import java.util.List;

/**
 *
 * Contains information about the current computer system. This includes the
 * architecture and type of the processor, the number of processors in the
 * system, the page size, and other such information.
 *
 * <pre>
 * typedef struct _SYSTEM_INFO {
 *   union {
 *     DWORD dwOemId;
 *     struct {
 *       WORD wProcessorArchitecture;
 *       WORD wReserved;
 *     } ;
 *   } ;
 *   DWORD     dwPageSize;
 *   LPVOID    lpMinimumApplicationAddress;
 *   LPVOID    lpMaximumApplicationAddress;
 *   DWORD_PTR dwActiveProcessorMask;
 *   DWORD     dwNumberOfProcessors;
 *   DWORD     dwProcessorType;
 *   DWORD     dwAllocationGranularity;
 *   WORD      wProcessorLevel;
 *   WORD      wProcessorRevision;
 * }SYSTEM_INFO;
 * </pre>
 *
 * @author Agustin Barto <abarto@gmail.com>
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms724958%28VS.85%29.aspx">SYSTEM_INFO Structure (Windows)</a>
 * @see <a href="http://msdn.microsoft.com/en-us/library/aa505945.aspx">Common Data Types</a>
 *
 */
public class SYSTEM_INFO extends Structure {

    @Override
    protected List getFieldOrder() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
        /**
         * A union to hold the processor architecture information.
         *
         * <pre>
         * union {
     *   DWORD dwOemId;
     *   struct {
     *     WORD wProcessorArchitecture;
     *     WORD wReserved;
     *   } ;
     * } ;
         * </pre>
         */
        public static class UnnamedUnion extends Union {
                /**
                 *
                 * Tagged inner class to indicate the value of an instance of the Union
                 * type is to be used in function invocations rather than its address.
                 *
                 * @see Union.ByValue
                 * @author Agustin Barto <abarto@gmail.com>
                 *
                 */
                public static class ByValue extends UnnamedUnion implements Union.ByValue { }

                /**
                 *
                 * Tagged inner class to indicate the address of an instance of the
                 * Union type is to be used within a Union definition rather than
                 * nesting the full Union contents.
                 *
                 * @see Union.ByReference
                 * @author Agustin Barto <abarto@gmail.com>
                 *
                 */
                public static class ByReference extends UnnamedUnion implements Union.ByReference { }
               
                /**
                 * A structure to hold the processor architecture information.
                 *
                 * <pre>
         * struct {
         *   WORD wProcessorArchitecture;
         *   WORD wReserved;
         * } ;
                 * </pre>
                 */
                public static abstract class UnnamedStructure extends Structure {

       /**
                         *
                         * Tagged inner class to indicate the value of an instance of the
                         * Structure type is to be used in function invocations rather than
                         * its address.
                         *
                         * @see Structure.ByValue
                         * @author Agustin Barto <abarto@gmail.com>
                         *
                         */
                        public static abstract class ByValue extends UnnamedStructure implements Structure.ByValue { }

                        /**
                         *
                         * Tagged inner class to indicate the address of an instance of the
                         * Structure type is to be used within a Structure definition rather
                         * than nesting the full Structure contents.
                         *
                         * @see Structure.ByReference
                         * @author Agustin Barto <abarto@gmail.com>
                         *
                         */
                        public static abstract class ByReference extends UnnamedStructure implements Structure.ByReference { }
                       
                        /**
                         * <p><code>WORD wProcessorArchitecture</code></p>
                         *
                         * The processor architecture of the installed operating system.
                         * This member can be one of the following values.
                         *
                         * <p></p>
                         *
                         * <table>
                         *   <tr>
                         *     <th>Value</th>
                         *     <th>Description</th>
                         *   </tr>
                         *   <tr>
                         *     <td>PROCESSOR_ARCHITECTURE_AMD64 - 9</td>
                         *     <td>x64 (AMD or Intel)</td>
                         *   <tr>
                         *   <tr>
                         *     <td>PROCESSOR_ARCHITECTURE_IA64 - 6</td>
                         *     <td>Intel Itanium Processor Family (IPF)</td>
                         *   <tr>
                         *   <tr>
                         *     <td>PROCESSOR_ARCHITECTURE_INTEL - 0</td>
                         *     <td>x86</td>
                         *   <tr>
                         *   <tr>
                         *     <td>PROCESSOR_ARCHITECTURE_UNKNOWN - 0xffff</td>
                         *     <td>Unknown architecture.</td>
                         *   <tr>
                         * </table>
                         *
                         */
                        public short wProcessorArchitecture;
                       
                        /**
                         * <p><code>WORD wReserved</code></p>
                         *
                         * This member is reserved for future use.
                         */
                        public short wReserved;
                }
               
                /**
                 * A field to hold the unnamed structure.
                 */
                public UnnamedStructure unnamedStructure = new UnnamedStructure.ByValue() {

                    @Override
                    protected List getFieldOrder() {
                        return null;
                        //To change body of generated methods, choose Tools | Templates.
                    }
                };
               
                /**
                 * <p><code>DWORD dwOemId</code></p>
                 *
                 * An obsolete member that is retained for compatibility. Applications
                 * should use the wProcessorArchitecture branch of the union.
                 */
                public int dwOemId;
        }
       
        /**
         * A field to hold the unnamed union.
         */
        public UnnamedUnion unnamedUnion = new UnnamedUnion.ByValue();
       
    /**
     * <p><code>DWORD dwPageSize</code></p>
     *
     * The page size and the granularity of page protection and commitment.
     * This is the page size used by the VirtualAlloc function.
     */
    public int dwPageSize;
   
        /**
         * <p><code>LPVOID lpMinimumApplicationAddress</code></p>
         *
         * A pointer to the lowest memory address accessible to applications and
         * dynamic-link libraries (DLLs).
         */
        public Pointer lpMinimumApplicationAddress;

        /**
         * <p><code>LPVOID lpMaximumApplicationAddress</code></p>
         *
         * A pointer to the highest memory address accessible to applications and
         * DLLs.
         */
        public Pointer lpMaximumApplicationAddress;

        /**
         * <p><code>DWORD_PTR dwActiveProcessorMask</code></p>
         *
         * A mask representing the set of processors configured into the system.
         * Bit 0 is processor 0; bit 31 is processor 31.
         */
        public IntByReference dwActiveProcessorMask;

        /**
         * <p><code>DWORD dwNumberOfProcessors</code></p>
         *
         * The number of physical processors in the system. To retrieve the number
         * of logical processors, use the GetLogicalProcessorInformation function.
         */
        public int dwNumberOfProcessors;

        /**
         * <p><code>DWORD dwProcessorType</code></p>
         *
         * An obsolete member that is retained for compatibility. Use the
         * wProcessorArchitecture, wProcessorLevel, and wProcessorRevision members
         * to determine the type of processor.
         *
         * <p></p>
         *
         * <table>
         *   <tr>
         *     <th>Name</th>
         *     <th>Value</th>
         *   </tr>
         *   <tr>
         *     <td>PROCESSOR_INTEL_386</td>
         *     <td>386</td>
         *   <tr>
         *   <tr>
         *     <td>PROCESSOR_INTEL_486</td>
         *     <td>486</td>
         *   <tr>
         *   <tr>
         *     <td>PROCESSOR_INTEL_PENTIUM</td>
         *     <td>586</td>
         *   <tr>
         *   <tr>
         *     <td>PROCESSOR_INTEL_IA64</td>
         *     <td>2200</td>
         *   <tr>
         *   <tr>
         *     <td>PROCESSOR_AMD_X8664</td>
         *     <td>8664</td>
         *   <tr>
         * </table>
         */
        public int dwProcessorType;

        /**
         * <p><code>DWORD dwAllocationGranularity</code></p>
         *
         * The granularity for the starting address at which virtual memory can be
         * allocated. For more information, see VirtualAlloc.
         */
        public int dwAllocationGranularity;

        /**
         * <p><code>WORD wProcessorLevel</code></p>
         *
         * The architecture-dependent processor level. It should be used only for
         * display purposes. To determine the feature set of a processor, use the
         * IsProcessorFeaturePresent function. If wProcessorArchitecture is
         * PROCESSOR_ARCHITECTURE_INTEL, wProcessorLevel is defined by the CPU
         * vendor. If wProcessorArchitecture is PROCESSOR_ARCHITECTURE_IA64,
         * wProcessorLevel is set to 1.
         */
        public short wProcessorLevel;

        /**
         * <p><code>WORD wProcessorRevision</code></p>
         *
         * The architecture-dependent processor revision. The following table shows
         * how the revision value is assembled for each type of processor
         * architecture.
         *
         * <table>
         *   <tr>
         *     <th>Processor</th>
         *     <th>Value</th>
         *   </tr>
         *   <tr>
         *     <td>Intel Pentium, Cyrix, or NextGen 586</td>
         *     <td>The high byte is the model and the low byte is the stepping. For
         *     example, if the value is xxyy, the model number and stepping can be
         *     displayed as follows: Model xx, Stepping yy.</td>
         *   </tr>
         *   <tr>
         *     <td>Intel 80386 or 80486</td>
         *     <td>A value of the form xxyz. If xx is equal to 0xFF, y - 0xA is the
         *     model number, and z is the stepping identifier. If xx is not equal
         *     to 0xFF, xx + 'A' is the stepping letter and yz is the minor
         *     stepping.</td>
         *   </tr>
         * </table>
         */
        public short wProcessorRevision;
}

